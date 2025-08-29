package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.PassengerItineraryPresenter;
import br.com.transoft.backend.dto.itinerary.ItineraryDto;
import br.com.transoft.backend.dto.itinerary.ItineraryPresenter;
import br.com.transoft.backend.entity.Driver;
import br.com.transoft.backend.entity.Itinerary;
import br.com.transoft.backend.entity.Passenger;
import br.com.transoft.backend.entity.PassengerStatus;
import br.com.transoft.backend.entity.route.Route;
import br.com.transoft.backend.exception.InvalidItineraryStatusException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.ItineraryRepository;
import br.com.transoft.backend.repository.PassengerStatusRepository;
import br.com.transoft.backend.utils.DateUtils;
import br.com.transoft.backend.constants.ItineraryStatus;
import br.com.transoft.backend.constants.ItineraryType;
import br.com.transoft.backend.constants.PassengerStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final PassengerService passengerService;
    private final RouteService routeService;
    private final PassengerStatusRepository passengerStatusRepository;

    public ItineraryService(ItineraryRepository itineraryRepository, PassengerService passengerService, RouteService routeService, PassengerStatusRepository passengerStatusRepository) {
        this.itineraryRepository = itineraryRepository;
        this.passengerService = passengerService;
        this.routeService = routeService;
        this.passengerStatusRepository = passengerStatusRepository;
    }

    @Transactional(rollbackOn = SQLException.class)
    public void generateItinerary(ItineraryDto itineraryDto) {
        Route route = routeService.findRouteById(itineraryDto.getRouteId());
        Driver driver = route.getDefaultDriver();
        Set<Passenger> passengers = route.getPassengers();

        List<LocalDate> dates = DateUtils.getDatesBetween(itineraryDto.getDateInterval().getStartDate(), itineraryDto.getDateInterval().getEndDate());

        for (LocalDate date : dates) {
            createDepartureTrip(date, route, driver, passengers);
            createReturnTrip(date, route, driver, passengers);
        }
    }

    @Transactional(rollbackOn = SQLException.class)
    protected void createDepartureTrip(LocalDate date, Route route, Driver driver, Set<Passenger> passengers) {
        Itinerary itinerary = Itinerary.builder()
                .itineraryId(UUID.randomUUID().toString())
                .type(ItineraryType.IDA)
                .status(ItineraryStatus.AGENDADO)
                .date(date)
                .startTime(route.getDepartureTrip().getStartTime())
                .endTime(route.getDepartureTrip().getEndTime())
                .route(route)
                .driver(driver)
                .build();

        itineraryRepository.save(itinerary);
        addPassengersToItinerary(passengers, itinerary);
    }

    @Transactional(rollbackOn = SQLException.class)
    protected void createReturnTrip(LocalDate date, Route route, Driver driver, Set<Passenger> passengers) {
        Itinerary itinerary = Itinerary.builder()
                .itineraryId(UUID.randomUUID().toString())
                .type(ItineraryType.VOLTA)
                .status(ItineraryStatus.AGENDADO)
                .date(date)
                .startTime(route.getDepartureTrip().getStartTime())
                .endTime(route.getDepartureTrip().getEndTime())
                .route(route)
                .driver(driver)
                .build();

        itineraryRepository.save(itinerary);
        addPassengersToItinerary(passengers, itinerary);
    }

    private void addPassengersToItinerary(Set<Passenger> passengers, Itinerary itinerary) {
        Set<PassengerStatus> passengersStatus = new HashSet<>();

        for (Passenger passenger : passengers) {
            PassengerStatus passengerStatus = PassengerStatus.builder()
                    .passenger(passenger)
                    .itinerary(itinerary)
                    .status(PassengerStatusEnum.CONFIRMADO)
                    .build();

            passengersStatus.add(passengerStatus);
        }

        passengerStatusRepository.saveAll(passengersStatus);
    }

    public Itinerary findItineraryById(String itineraryId) {
        return itineraryRepository.findById(itineraryId).orElseThrow(() -> new ResourceNotFoundException("Itinerary not found"));
    }

    public List<ItineraryPresenter> listItineraries(int page, int size) {
        return itineraryRepository.findAll(PageRequest.of(page, size)).stream().map(Itinerary::toPresenter).toList();
    }

    @Transactional(rollbackOn = SQLException.class)
    public String changeItineraryStatus(String itineraryId, String newStatus) {
        Itinerary itinerary = findItineraryById(itineraryId);

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
    public List<PassengerItineraryPresenter> changePassengerStatus(String itineraryId, String passengerId, String newStatus) {
        PassengerStatusEnum newPassengerStatus = PassengerStatusEnum.fromString(newStatus);

        Itinerary itinerary = findItineraryById(itineraryId);
        Passenger passenger = passengerService.findPassengerById(passengerId);

        PassengerStatus passengerStatus = passengerStatusRepository
                .findByItineraryAndPassenger(itinerary, passenger)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger is not in this itinerary."));

        passengerStatus.setStatus(newPassengerStatus);

        passengerStatusRepository.save(passengerStatus);

        return listItinerariesFromItinerary(itinerary);
    }

    public List<PassengerItineraryPresenter> listPassengersFromItinerary(String itineraryId) {
        Itinerary itinerary = findItineraryById(itineraryId);
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
