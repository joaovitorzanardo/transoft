package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.PassengerItineraryPresenter;
import br.com.transoft.backend.dto.itinerary.ItineraryDto;
import br.com.transoft.backend.dto.itinerary.ItineraryPresenter;
import br.com.transoft.backend.entity.Driver;
import br.com.transoft.backend.entity.Itinerary;
import br.com.transoft.backend.entity.Passenger;
import br.com.transoft.backend.entity.PassengerItinerary;
import br.com.transoft.backend.entity.route.Route;
import br.com.transoft.backend.exception.InvalidItineraryStatusException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.ItineraryRepository;
import br.com.transoft.backend.repository.PassengerItineraryRepository;
import br.com.transoft.backend.repository.PassengerRepository;
import br.com.transoft.backend.repository.RouteRepository;
import br.com.transoft.backend.utils.DateUtils;
import br.com.transoft.backend.utils.ItineraryStatus;
import br.com.transoft.backend.utils.ItineraryType;
import br.com.transoft.backend.utils.PassengerItineraryStatus;
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
    private final PassengerRepository passengerRepository;
    private final RouteRepository routeRepository;
    private final PassengerItineraryRepository passengerItineraryRepository;

    public ItineraryService(ItineraryRepository itineraryRepository, PassengerRepository passengerRepository, RouteRepository routeRepository, PassengerItineraryRepository passengerItineraryRepository) {
        this.itineraryRepository = itineraryRepository;
        this.passengerRepository = passengerRepository;
        this.routeRepository = routeRepository;
        this.passengerItineraryRepository = passengerItineraryRepository;
    }

    @Transactional(rollbackOn = SQLException.class)
    public void generateItinerary(ItineraryDto itineraryDto) {
        Route route = this.routeRepository.findById(itineraryDto.getRouteId()).orElseThrow(() -> new ResourceNotFoundException("Route not found"));
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

        this.itineraryRepository.save(itinerary);

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

        this.itineraryRepository.save(itinerary);

        addPassengersToItinerary(passengers, itinerary);
    }

    private void addPassengersToItinerary(Set<Passenger> passengers, Itinerary itinerary) {
        Set<PassengerItinerary> passengerItineraries = new HashSet<>();

        for (Passenger passenger : passengers) {
            PassengerItinerary passengerItinerary = PassengerItinerary.builder()
                    .passengerItineraryId(UUID.randomUUID().toString())
                    .passenger(passenger)
                    .itinerary(itinerary)
                    .passengerItineraryStatus(PassengerItineraryStatus.CONFIRMADO)
                    .build();

            passengerItineraries.add(passengerItinerary);
        }

        this.passengerItineraryRepository.saveAll(passengerItineraries);
    }

    public List<ItineraryPresenter> listItineraries(int page, int size) {
        return this.itineraryRepository.findAll(PageRequest.of(page, size)).stream().map(Itinerary::toPresenter).toList();
    }

    public ItineraryPresenter listItineraryById(String itineraryId) {
        return this.itineraryRepository.findById(itineraryId).orElseThrow(() -> new ResourceNotFoundException("Itinerary not found")).toPresenter();
    }

    @Transactional(rollbackOn = Exception.class)
    public String changeItineraryStatus(String itineraryId, String newStatus) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow(() -> new ResourceNotFoundException("Itinerary not found"));

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

    @Transactional(rollbackOn = Exception.class)
    public List<PassengerItineraryPresenter> changePassengerStatus(String itineraryId, String passengerId, String newStatus) {
        PassengerItineraryStatus newPassengerItineraryStatus = PassengerItineraryStatus.fromString(newStatus);

        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow(() -> new ResourceNotFoundException("Itinerary not found"));
        Passenger passenger = passengerRepository.findById(passengerId).orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        PassengerItinerary passengerItinerary = passengerItineraryRepository
                .findByItineraryAndPassenger(itinerary, passenger)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger is not in this itinerary."));

        passengerItinerary.setPassengerItineraryStatus(newPassengerItineraryStatus);

        passengerItineraryRepository.save(passengerItinerary);

        return listItinerariesFromItinerary(itinerary);
    }

    public List<PassengerItineraryPresenter> listPassengersFromItinerary(String itineraryId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow(() -> new ResourceNotFoundException("Itinerary not found"));
        return listItinerariesFromItinerary(itinerary);
    }

    private List<PassengerItineraryPresenter> listItinerariesFromItinerary(Itinerary itinerary) {
        return passengerItineraryRepository.findByItinerary(itinerary)
                .stream()
                .map(pi -> new PassengerItineraryPresenter(
                        pi.getPassenger().toPresenter(),
                        pi.getPassengerItineraryStatus().getStatus()
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
