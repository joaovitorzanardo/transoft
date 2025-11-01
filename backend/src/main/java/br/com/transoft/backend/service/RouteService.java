package br.com.transoft.backend.service;

import br.com.transoft.backend.constants.ItineraryType;
import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.route.*;
import br.com.transoft.backend.entity.*;
import br.com.transoft.backend.entity.route.DayOfTheWeek;
import br.com.transoft.backend.entity.route.DepartureTrip;
import br.com.transoft.backend.entity.route.ReturnTrip;
import br.com.transoft.backend.entity.route.Route;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.ItineraryRepository;
import br.com.transoft.backend.repository.PassengerRepository;
import br.com.transoft.backend.repository.RouteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final ItineraryRepository itineraryRepository;
    private final SchoolService schoolService;
    private final DriverService driverService;
    private final VehicleService vehicleService;

    public RouteService(RouteRepository routeRepository, ItineraryRepository itineraryRepository, SchoolService schoolService, DriverService driverService, VehicleService vehicleService) {
        this.routeRepository = routeRepository;
        this.itineraryRepository = itineraryRepository;
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
                .dayOfTheWeek(new DayOfTheWeek(routeDto.getDaysOfWeek().isMonday(), routeDto.getDaysOfWeek().isTuesday(), routeDto.getDaysOfWeek().isWednesday(), routeDto.getDaysOfWeek().isThursday(), routeDto.getDaysOfWeek().isFriday()))
                .build();

        routeRepository.save(route);
    }

    @Transactional(rollbackOn = SQLException.class)
    public void updateRoute(String routeId, RouteUpdateDto routeUpdateDto, LoggedUserAccount loggedUserAccount) {
        Route route = routeRepository
                .findByRouteIdAndCompany_CompanyId(routeId, loggedUserAccount.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada"));

        if (!Objects.equals(route.getDefaultDriver().getDriverId(), routeUpdateDto.getDefaultDriverId())) {
            Driver driver = driverService.findDriverById(routeUpdateDto.getDefaultDriverId(), loggedUserAccount);
            route.setDefaultDriver(driver);
        }

        if (!Objects.equals(route.getDefaultVehicle().getVehicleId(), routeUpdateDto.getDefaultVehicleId())) {
            Vehicle vehicle = vehicleService.findVehicleById(routeUpdateDto.getDefaultVehicleId(), loggedUserAccount);
            route.setDefaultVehicle(vehicle);
        }

        route.setName(routeUpdateDto.getName());
        route.setDepartureTrip(new DepartureTrip(routeUpdateDto.getDepartureTrip().getStartTime(), routeUpdateDto.getDepartureTrip().getEndTime()));
        route.setReturnTrip(new ReturnTrip(routeUpdateDto.getReturnTrip().getStartTime(), routeUpdateDto.getReturnTrip().getEndTime()));
        route.setDayOfTheWeek(new DayOfTheWeek(
                routeUpdateDto.getDaysOfWeek().isMonday(),
                routeUpdateDto.getDaysOfWeek().isTuesday(),
                routeUpdateDto.getDaysOfWeek().isWednesday(),
                routeUpdateDto.getDaysOfWeek().isThursday(),
                routeUpdateDto.getDaysOfWeek().isFriday()
        ));

        routeRepository.save(route);

        if (routeUpdateDto.isUpdateItineraries()) {
            updateRouteItineraries(loggedUserAccount.companyId(), routeId, route);
        }
    }

    @Transactional(rollbackOn = SQLException.class)
    protected void updateRouteItineraries(String companyId, String routeId, Route newRoute) {
        List<Itinerary> itinerariesToUpdate = itineraryRepository.findAllScheduledItinerariesByRouteId(companyId, routeId);

        for (Itinerary itinerary : itinerariesToUpdate) {
            itinerary.setDriver(newRoute.getDefaultDriver());
            itinerary.setVehicle(newRoute.getDefaultVehicle());

            if (ItineraryType.IDA.equals( itinerary.getType())) {
                itinerary.setStartTime(newRoute.getDepartureTrip().getStartTime());
                itinerary.setEndTime(newRoute.getDepartureTrip().getEndTime());
            }

            if (ItineraryType.VOLTA.equals( itinerary.getType())) {
                itinerary.setStartTime(newRoute.getReturnTrip().getStartTime());
                itinerary.setEndTime(newRoute.getReturnTrip().getEndTime());
            }
        }

        itineraryRepository.saveAll(itinerariesToUpdate);
    }

    public Route findRouteById(String routeId, LoggedUserAccount loggedUserAccount) {
        return routeRepository.findByRouteIdAndCompany_CompanyId(routeId, loggedUserAccount.companyId()).orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada"));
    }

    public RoutePresenterList listRoutes(int page, int size, LoggedUserAccount loggedUserAccount) {
        List<RoutePresenter> routes = routeRepository.findAllByCompany_CompanyId(loggedUserAccount.companyId(), PageRequest.of(page, size)).stream().map(Route::toPresenter).collect(Collectors.toList());
        int count = routeRepository.countAllByCompany_CompanyId(loggedUserAccount.companyId());

        return new RoutePresenterList(count, routes);
    }

    public List<RouteSelectPresenter> listAllRoutes(LoggedUserAccount loggedUserAccount) {
        return routeRepository.findAllByCompany_CompanyId(loggedUserAccount.companyId()).stream().map(route -> new RouteSelectPresenter(route.getRouteId(), route.getName())).collect(Collectors.toList());
    }

    public List<RouteSelectPresenter> listAllActiveRoutes(LoggedUserAccount loggedUserAccount) {
        return routeRepository.findAllByCompany_CompanyIdAndActiveTrue(loggedUserAccount.companyId())
                .stream()
                .map(route -> new RouteSelectPresenter(route.getRouteId(), route.getName()))
                .collect(Collectors.toList());
    }

    public RoutesStatsPresenter getRoutesStats(LoggedUserAccount loggedUserAccount) {
        int total = routeRepository.countAllByCompany_CompanyId(loggedUserAccount.companyId());
        int active = routeRepository.countAllByCompany_CompanyIdAndActive(loggedUserAccount.companyId(), true);
        int inactive = routeRepository.countAllByCompany_CompanyIdAndActive(loggedUserAccount.companyId(), false);

        return new RoutesStatsPresenter(total, active, inactive);
    }

    public RoutePresenter listRouteById(String routeId, LoggedUserAccount loggedUserAccount) {
        return routeRepository.findByRouteIdAndCompany_CompanyId(routeId, loggedUserAccount.companyId()).orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada")).toPresenter();
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
