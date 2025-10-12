package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.route.*;
import br.com.transoft.backend.entity.*;
import br.com.transoft.backend.entity.route.DayOfWeek;
import br.com.transoft.backend.entity.route.DepartureTrip;
import br.com.transoft.backend.entity.route.ReturnTrip;
import br.com.transoft.backend.entity.route.Route;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.PassengerRepository;
import br.com.transoft.backend.repository.RouteRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final PassengerRepository passengerRepository;
    private final SchoolService schoolService;
    private final DriverService driverService;
    private final VehicleService vehicleService;

    public RouteService(RouteRepository routeRepository, PassengerRepository passengerRepository, SchoolService schoolService, DriverService driverService, VehicleService vehicleService) {
        this.routeRepository = routeRepository;
        this.passengerRepository = passengerRepository;
        this.schoolService = schoolService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
    }

    public void saveRoute(RouteDto routeDto, LoggedUserAccount loggedUserAccount) {
        School school = schoolService.findSchoolById(routeDto.getSchoolId());
        Driver driver = driverService.findDriverById(routeDto.getDefaultDriverId(), loggedUserAccount);
        Vehicle vehicle = vehicleService.findVehicleById(routeDto.getDefaultVehicleId(), loggedUserAccount);

        Route route = Route.builder()
                .routeId(UUID.randomUUID().toString())
                .name(routeDto.getName())
                .school(school)
                .defaultDriver(driver)
                .defaultVehicle(vehicle)
                .active(true)
                .company(new Company(loggedUserAccount.companyId()))
                .departureTrip(new DepartureTrip(routeDto.getDepartureTrip().getStartTime(), routeDto.getDepartureTrip().getEndTime()))
                .returnTrip(new ReturnTrip(routeDto.getReturnTrip().getStartTime(), routeDto.getReturnTrip().getEndTime()))
                .dayOfWeek(new DayOfWeek(routeDto.getDaysOfWeek().isMonday(), routeDto.getDaysOfWeek().isTuesday(), routeDto.getDaysOfWeek().isWednesday(), routeDto.getDaysOfWeek().isThursday(), routeDto.getDaysOfWeek().isFriday()))
                .build();

        routeRepository.save(route);
    }

    public Route findRouteById(String routeId, LoggedUserAccount loggedUserAccount) {
        return routeRepository.findByRouteIdAndCompany_CompanyId(routeId, loggedUserAccount.companyId()).orElseThrow(() -> new ResourceNotFoundException("Route not found"));
    }

    public RoutePresenterList listRoutes(int page, int size, LoggedUserAccount loggedUserAccount) {
        List<RoutePresenter> routes = routeRepository.findAllByCompany_CompanyId(loggedUserAccount.companyId(), PageRequest.of(page, size)).stream().map(Route::toPresenter).collect(Collectors.toList());
        int count = routeRepository.countAllByCompany_CompanyId(loggedUserAccount.companyId());

        return new RoutePresenterList(count, routes);
    }

    public List<RouteSelectPresenter> listAllRoutes(LoggedUserAccount loggedUserAccount) {
        return routeRepository.findAllByCompany_CompanyId(loggedUserAccount.companyId()).stream().map(route -> new RouteSelectPresenter(route.getRouteId(), route.getName())).collect(Collectors.toList());
    }

    public RoutesStatsPresenter getRoutesStats(LoggedUserAccount loggedUserAccount) {
        int total = routeRepository.countAllByCompany_CompanyId(loggedUserAccount.companyId());
        int active = routeRepository.countAllByCompany_CompanyIdAndActive(loggedUserAccount.companyId(), true);
        int inactive = routeRepository.countAllByCompany_CompanyIdAndActive(loggedUserAccount.companyId(), false);

        return new RoutesStatsPresenter(total, active, inactive);
    }

    public RoutePresenter listRouteById(String routeId, LoggedUserAccount loggedUserAccount) {
        return routeRepository.findByRouteIdAndCompany_CompanyId(routeId, loggedUserAccount.companyId()).orElseThrow(() -> new ResourceNotFoundException("Route not found")).toPresenter();
    }

    public void addPassengerToRoute(String passengerId, String routeId, LoggedUserAccount loggedUserAccount) {
        Route route = findRouteById(routeId, loggedUserAccount);
        Passenger passenger = passengerRepository.findByPassengerIdAndCompany_CompanyId(passengerId, loggedUserAccount.companyId()).orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        if (!Objects.isNull(passenger.getRoute())) {
            throw new ResourceConflictException("Passenger is already in a route");
        }

        passenger.setRoute(route);
        passengerRepository.save(passenger);
    }

    public void removePassengerFromRoute(String passengerId, String routeId, LoggedUserAccount loggedUserAccount) {
        Route route = findRouteById(routeId, loggedUserAccount);
        Passenger passenger = passengerRepository.findByPassengerIdAndCompany_CompanyId(passengerId, loggedUserAccount.companyId()).orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        if (!passenger.getRoute().equals(route)) {
            throw new ResourceNotFoundException("Passenger is not in this route");
        }

        passenger.setRoute(null);
        passengerRepository.save(passenger);
    }

    public void enableRoute(String routeId, LoggedUserAccount loggedUserAccount) {
        Route route = findRouteById(routeId, loggedUserAccount);
        route.setActive(true);
        routeRepository.save(route);
    }

    public void disableRoute(String routeId, LoggedUserAccount loggedUserAccount) {
        Route route = findRouteById(routeId, loggedUserAccount);
        route.setActive(false);
        routeRepository.save(route);
    }

}
