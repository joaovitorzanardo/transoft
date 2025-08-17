package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.route.RouteDto;
import br.com.transoft.backend.dto.route.RoutePresenter;
import br.com.transoft.backend.entity.Driver;
import br.com.transoft.backend.entity.School;
import br.com.transoft.backend.entity.route.DayOfWeek;
import br.com.transoft.backend.entity.route.DepartureTrip;
import br.com.transoft.backend.entity.route.ReturnTrip;
import br.com.transoft.backend.entity.route.Route;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.DriverRepository;
import br.com.transoft.backend.repository.RouteRepository;
import br.com.transoft.backend.repository.SchoolRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final SchoolRepository schoolRepository;
    private final DriverRepository driverRepository;

    public RouteService(RouteRepository routeRepository, SchoolRepository schoolRepository, DriverRepository driverRepository) {
        this.routeRepository = routeRepository;
        this.schoolRepository = schoolRepository;
        this.driverRepository = driverRepository;
    }

    public void saveRoute(RouteDto routeDto) {
        School school = this.schoolRepository.findById(routeDto.getSchoolId()).orElseThrow(() -> new ResourceNotFoundException("School not found"));
        Driver driver = this.driverRepository.findById(routeDto.getDefaultDriverId()).orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        Route route = Route.builder()
                .routeId(UUID.randomUUID().toString())
                .name(routeDto.getName())
                .school(school)
                .defaultDriver(driver)
                .active(true)
                .departureTrip(new DepartureTrip(routeDto.getDepartureTrip().getStartTime(), routeDto.getDepartureTrip().getEndTime()))
                .returnTrip(new ReturnTrip(routeDto.getReturnTrip().getStartTime(), routeDto.getReturnTrip().getEndTime()))
                .dayOfWeek(new DayOfWeek(routeDto.getDaysOfWeek().isMonday(), routeDto.getDaysOfWeek().isTuesday(), routeDto.getDaysOfWeek().isWednesday(), routeDto.getDaysOfWeek().isThursday(), routeDto.getDaysOfWeek().isFriday()))
                .build();

        this.routeRepository.save(route);
    }

    public List<RoutePresenter> listRoutes(int page, int size) {
        return this.routeRepository.findAll(PageRequest.of(page, size)).stream().map(Route::toPresenter).collect(Collectors.toList());
    }

    public RoutePresenter listRouteById(String routeId) {
        return this.routeRepository.findById(routeId).orElseThrow(() -> new ResourceNotFoundException("Route not found")).toPresenter();
    }

    public void updateRoute(String routeId, RouteDto routeDto) {

    }

    public void enableRoute(String routeId) {
        Route route = this.routeRepository.findById(routeId).orElseThrow(() -> new ResourceNotFoundException("Route not found"));
        route.setActive(true);
        this.routeRepository.save(route);
    }

    public void disableRoute(String routeId) {
        Route route = this.routeRepository.findById(routeId).orElseThrow(() -> new ResourceNotFoundException("Route not found"));
        route.setActive(false);
        this.routeRepository.save(route);
    }

}
