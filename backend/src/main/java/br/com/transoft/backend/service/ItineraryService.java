package br.com.transoft.backend.service;

import br.com.transoft.backend.constants.Role;
import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.itinerary.*;
import br.com.transoft.backend.dto.itinerary.account.ItineraryAccount;
import br.com.transoft.backend.dto.itinerary.account.ItineraryAccountView;
import br.com.transoft.backend.entity.*;
import br.com.transoft.backend.entity.route.Route;
import br.com.transoft.backend.exception.InvalidItineraryStatusException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.ItineraryQueryRepository;
import br.com.transoft.backend.repository.ItineraryRepository;
import br.com.transoft.backend.repository.PassengerStatusRepository;
import br.com.transoft.backend.repository.UserAccountRepository;
import br.com.transoft.backend.utils.DateUtils;
import br.com.transoft.backend.constants.ItineraryStatus;
import br.com.transoft.backend.constants.ItineraryType;
import br.com.transoft.backend.constants.PassengerStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final PassengerService passengerService;
    private final RouteService routeService;
    private final PassengerStatusRepository passengerStatusRepository;
    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final ItineraryQueryRepository itineraryQueryRepository;

    public ItineraryService(ItineraryRepository itineraryRepository, PassengerService passengerService, RouteService routeService, PassengerStatusRepository passengerStatusRepository, DriverService driverService, VehicleService vehicleService, ItineraryQueryRepository itineraryQueryRepository) {
        this.itineraryRepository = itineraryRepository;
        this.passengerService = passengerService;
        this.routeService = routeService;
        this.passengerStatusRepository = passengerStatusRepository;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.itineraryQueryRepository = itineraryQueryRepository;
    }

    @Transactional(rollbackOn = SQLException.class)
    public void generateItinerary(GenerateItineraryDto generateItineraryDto, LoggedUserAccount loggedUserAccount) {
        Route route = routeService.findRouteById(generateItineraryDto.getRouteId(), loggedUserAccount);
        Driver driver = route.getDefaultDriver();
        Vehicle vehicle = route.getDefaultVehicle();
        Set<Passenger> passengers = route.getPassengers();

        List<LocalDate> dates = DateUtils.getDatesBetween(generateItineraryDto.getDateInterval().getStartDate(), generateItineraryDto.getDateInterval().getEndDate());

        for (LocalDate date : dates) {
            createDepartureTrip(date, route, driver, vehicle, passengers, loggedUserAccount.companyId());
            createReturnTrip(date, route, driver, vehicle, passengers, loggedUserAccount.companyId());
        }
    }

    @Transactional(rollbackOn = SQLException.class)
    protected void createDepartureTrip(LocalDate date, Route route, Driver driver, Vehicle vehicle, Set<Passenger> passengers, String companyId) {
        Itinerary itinerary = Itinerary.builder()
                .itineraryId(UUID.randomUUID().toString())
                .company(new Company(companyId))
                .type(ItineraryType.IDA)
                .status(ItineraryStatus.AGENDADO)
                .date(date)
                .startTime(route.getDepartureTrip().getStartTime())
                .endTime(route.getDepartureTrip().getEndTime())
                .route(route)
                .driver(driver)
                .vehicle(vehicle)
                .build();

        itineraryRepository.save(itinerary);
        addPassengersToItinerary(passengers, itinerary);
    }

    @Transactional(rollbackOn = SQLException.class)
    protected void createReturnTrip(LocalDate date, Route route, Driver driver, Vehicle vehicle, Set<Passenger> passengers, String companyId) {
        Itinerary itinerary = Itinerary.builder()
                .itineraryId(UUID.randomUUID().toString())
                .company(new Company(companyId))
                .type(ItineraryType.VOLTA)
                .status(ItineraryStatus.AGENDADO)
                .date(date)
                .startTime(route.getDepartureTrip().getStartTime())
                .endTime(route.getDepartureTrip().getEndTime())
                .route(route)
                .driver(driver)
                .vehicle(vehicle)
                .build();

        itineraryRepository.save(itinerary);
        addPassengersToItinerary(passengers, itinerary);
    }

    private void addPassengersToItinerary(Set<Passenger> passengers, Itinerary itinerary) {
        Set<PassengerStatus> passengersStatus = new HashSet<>();

        for (Passenger passenger : passengers) {
            PassengerStatusKey passengerStatusKey = PassengerStatusKey.builder()
                    .passengerId(passenger.getPassengerId())
                    .itineraryId(itinerary.getItineraryId())
                    .build();

            PassengerStatus passengerStatus = PassengerStatus.builder()
                    .passengerStatusKey(passengerStatusKey)
                    .passenger(passenger)
                    .itinerary(itinerary)
                    .status(PassengerStatusEnum.CONFIRMADO)
                    .build();

            passengersStatus.add(passengerStatus);
        }

        passengerStatusRepository.saveAll(passengersStatus);
    }

    public Itinerary findItineraryById(String itineraryId, LoggedUserAccount loggedUserAccount) {
        return itineraryRepository.findItineraryByItineraryIdAndCompany_CompanyId(itineraryId, loggedUserAccount.companyId()).orElseThrow(() -> new ResourceNotFoundException("Itinerary not found"));
    }

    public ItineraryPresenterList listItineraries(int page, int size, LoggedUserAccount loggedUserAccount) {
        List<ItineraryPresenter> itineraries = itineraryRepository.findAllByCompany_CompanyId(loggedUserAccount.companyId(), PageRequest.of(page, size)).stream().map(Itinerary::toPresenter).toList();
        int count = itineraryRepository.countItineraryByCompany_CompanyId(loggedUserAccount.companyId());

        return new ItineraryPresenterList(count, itineraries);
    }

    public ItineraryAccount getNextItinerary(LoggedUserAccount loggedUserAccount) {
        if (Role.PASSENGER.equals(loggedUserAccount.role())) {
            Passenger passenger = passengerService.findPassengerByUserAccountId(loggedUserAccount.userAccountId());

            Itinerary itinerary = itineraryRepository
                    .findNextItineraryForPassenger(passenger.getPassengerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Itinerary not found"));

            return new ItineraryAccount(itinerary.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), List.of(itinerary.toAccountView()));
        }

        if (Role.DRIVER.equals(loggedUserAccount.role())) {
            Driver driver = driverService.findDriverByUserAccountId(loggedUserAccount.userAccountId());

            Itinerary itinerary = itineraryRepository
                    .findNextItineraryForDriver(driver.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Itinerary not found"));

            return new ItineraryAccount(itinerary.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), List.of(itinerary.toAccountView()));
        }

        throw new ResourceNotFoundException("Itineraries not found");
    }

    public List<ItineraryAccount> listItinerariesByAccount(int page, int size, LoggedUserAccount loggedUserAccount) {
        if (Role.PASSENGER.equals(loggedUserAccount.role())) {
            Passenger passenger = passengerService.findPassengerByUserAccountId(loggedUserAccount.userAccountId());

            Map<LocalDate, List<Itinerary>> itineraries = itineraryRepository
                    .findItinerariesByPassengerId(passenger.getPassengerId(), PageRequest.of(page, size))
                    .stream()
                    .collect(Collectors.groupingBy(
                            Itinerary::getDate,
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));

            return convertToItineraryAccount(itineraries);
        }

        if (Role.DRIVER.equals(loggedUserAccount.role())) {
            Driver driver = driverService.findDriverByUserAccountId(loggedUserAccount.userAccountId());

            Map<LocalDate, List<Itinerary>> itineraries = itineraryRepository
                    .findItinerariesByDriverId(driver.getDriverId(), PageRequest.of(page, size))
                    .stream()
                    .collect(Collectors.groupingBy(Itinerary::getDate));

            return convertToItineraryAccount(itineraries);
        }

        throw new ResourceNotFoundException("Itineraries not found");
    }

    public List<ItineraryAccount> listItinerariesHistoryByAccount(int page, int size, LoggedUserAccount loggedUserAccount) {
        if (Role.PASSENGER.equals(loggedUserAccount.role())) {
            Passenger passenger = passengerService.findPassengerByUserAccountId(loggedUserAccount.userAccountId());

            Map<LocalDate, List<Itinerary>> itineraries = itineraryRepository
                    .findItinerariesHistoryByPassengerId(passenger.getPassengerId(), PageRequest.of(page, size))
                    .stream()
                    .collect(Collectors.groupingBy(Itinerary::getDate));

            return convertToItineraryAccount(itineraries);
        }

        if (Role.DRIVER.equals(loggedUserAccount.role())) {
            Driver driver = driverService.findDriverByUserAccountId(loggedUserAccount.userAccountId());

            Map<LocalDate, List<Itinerary>> itineraries = itineraryRepository
                    .findItinerariesHistoryByDriverId(driver.getDriverId(), PageRequest.of(page, size))
                    .stream()
                    .collect(Collectors.groupingBy(Itinerary::getDate));

            return convertToItineraryAccount(itineraries);
        }

        throw new ResourceNotFoundException("Itineraries not found");
    }

    public ItineraryPresenterList listItinerariesWithFilter(ItineraryFilter itineraryFilter, int page, int size, LoggedUserAccount loggedUserAccount) {
        List<ItineraryPresenter> itineraries = itineraryQueryRepository.findByFilter(itineraryFilter, loggedUserAccount.companyId())
                .stream()
                .map(Itinerary::toPresenter)
                .toList();
        int count = itineraryRepository.countItineraryByCompany_CompanyId(loggedUserAccount.companyId());

        return new ItineraryPresenterList(count, itineraries);
    }

    private List<ItineraryAccount> convertToItineraryAccount(Map<LocalDate, List<Itinerary>> itineraries) {
        return itineraries.entrySet().stream()
                .map(entry -> new ItineraryAccount(
                        entry.getKey().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        entry.getValue().stream()
                                .map(Itinerary::toAccountView)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public ItineraryStatsPresenter getItinerariesStats(LoggedUserAccount loggedUserAccount) {
        int total = itineraryRepository.countItineraryByCompany_CompanyId(loggedUserAccount.companyId());
        int scheduled = itineraryRepository.countItineraryByCompany_CompanyIdAndStatus(loggedUserAccount.companyId(), ItineraryStatus.AGENDADO);
        int finished = itineraryRepository.countItineraryByCompany_CompanyIdAndStatus(loggedUserAccount.companyId(), ItineraryStatus.CONCLUIDO);
        int canceled = itineraryRepository.countItineraryByCompany_CompanyIdAndStatus(loggedUserAccount.companyId(), ItineraryStatus.CANCELADO);

        return new ItineraryStatsPresenter(total, scheduled, finished, canceled);
    }

    public void updateItinerary(String itineraryId, ItineraryDto itineraryDto, LoggedUserAccount loggedUserAccount) {
        Itinerary itinerary = findItineraryById(itineraryId, loggedUserAccount);
        Driver driver = driverService.findDriverById(itineraryDto.getDriverId(), loggedUserAccount);
        Vehicle vehicle = vehicleService.findVehicleById(itineraryDto.getVehicleId(), loggedUserAccount);

        itinerary.setDriver(driver);
        itinerary.setVehicle(vehicle);

        itinerary.setStartTime(itineraryDto.getStartTime());
        itinerary.setEndTime(itineraryDto.getEndTime());

        itineraryRepository.save(itinerary);
    }

    public void cancelItinerary(String itineraryId, LoggedUserAccount loggedUserAccount) {
        Itinerary itinerary = findItineraryById(itineraryId, loggedUserAccount);

        itinerary.setStatus(ItineraryStatus.CANCELADO);

        itineraryRepository.save(itinerary);
    }

    @Transactional(rollbackOn = SQLException.class)
    public String changeItineraryStatus(String itineraryId, String newStatus, LoggedUserAccount loggedUserAccount) {
        Itinerary itinerary = findItineraryById(itineraryId, loggedUserAccount);

        ItineraryStatus newItineraryStatus = ItineraryStatus.fromString(newStatus);

        if (itinerary.getStatus().equals(newItineraryStatus)) {
            return newItineraryStatus.getStatus();
        }

        if (!isNewStatusValid(itinerary.getStatus(), newItineraryStatus)) {
            throw new InvalidItineraryStatusException("O novo status nao é válido!");
        }

        itinerary.setStatus(newItineraryStatus);

        itineraryRepository.save(itinerary);

        return newItineraryStatus.getStatus();
    }

    @Transactional(rollbackOn = SQLException.class)
    public List<PassengerItineraryPresenter> changePassengerStatus(String itineraryId, String passengerId, String newStatus, LoggedUserAccount loggedUserAccount) {
        PassengerStatusEnum newPassengerStatus = PassengerStatusEnum.fromString(newStatus);

        Itinerary itinerary = findItineraryById(itineraryId, loggedUserAccount);
        Passenger passenger = passengerService.findPassengerById(passengerId, loggedUserAccount);

        PassengerStatus passengerStatus = passengerStatusRepository
                .findByItineraryAndPassenger(itinerary, passenger)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger is not in this itinerary."));

        passengerStatus.setStatus(newPassengerStatus);

        passengerStatusRepository.save(passengerStatus);

        return listItinerariesFromItinerary(itinerary);
    }


    public void cancelPassengerItinerary(String itineraryId, LoggedUserAccount loggedUserAccount) {
        Itinerary itinerary = findItineraryById(itineraryId, loggedUserAccount);
        Passenger passenger = passengerService.findPassengerByUserAccountId(loggedUserAccount.userAccountId());

        PassengerStatus passengerStatus = passengerStatusRepository
                .findByItineraryAndPassenger(itinerary, passenger)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger is not in this itinerary."));

        passengerStatus.setStatus(PassengerStatusEnum.NAO_VAI);

        passengerStatusRepository.save(passengerStatus);
    }

    public void confirmPassengerItinerary(String itineraryId, LoggedUserAccount loggedUserAccount) {
        Itinerary itinerary = findItineraryById(itineraryId, loggedUserAccount);
        Passenger passenger = passengerService.findPassengerByUserAccountId(loggedUserAccount.userAccountId());

        PassengerStatus passengerStatus = passengerStatusRepository
                .findByItineraryAndPassenger(itinerary, passenger)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger is not in this itinerary."));

        passengerStatus.setStatus(PassengerStatusEnum.CONFIRMADO);

        passengerStatusRepository.save(passengerStatus);
    }

    public List<PassengerItineraryPresenter> listPassengersFromItinerary(String itineraryId, LoggedUserAccount loggedUserAccount) {
        Itinerary itinerary = findItineraryById(itineraryId, loggedUserAccount);
        return listItinerariesFromItinerary(itinerary);
    }

    private List<PassengerItineraryPresenter> listItinerariesFromItinerary(Itinerary itinerary) {
        return passengerStatusRepository.findByItinerary(itinerary)
                .stream()
                .map(pi -> new PassengerItineraryPresenter(
                        pi.getPassenger().toPresenter(),
                        pi.getStatus().getStatus()
                ))
                .toList();
    }

    private boolean isNewStatusValid(ItineraryStatus oldStatus, ItineraryStatus newStatus) {
        if (oldStatus == ItineraryStatus.AGENDADO && newStatus == ItineraryStatus.EM_ANDAMENTO) {
            return true;
        }

        return oldStatus == ItineraryStatus.EM_ANDAMENTO && newStatus == ItineraryStatus.CONCLUIDO;
    }

}
