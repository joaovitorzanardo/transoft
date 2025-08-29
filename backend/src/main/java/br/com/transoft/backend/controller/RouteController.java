package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.passenger.PassengerPresenter;
import br.com.transoft.backend.dto.route.RouteDto;
import br.com.transoft.backend.dto.route.RoutePresenter;
import br.com.transoft.backend.service.PassengerService;
import br.com.transoft.backend.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/routes")
public class RouteController {

    private final RouteService routeService;
    private final PassengerService passengerService;

    public RouteController(RouteService routeService, PassengerService passengerService) {
        this.routeService = routeService;
        this.passengerService = passengerService;
    }

    @PostMapping
    public void saveRoute(@Valid @RequestBody RouteDto routeDto) {
        routeService.saveRoute(routeDto);
    }

    @GetMapping
    public List<RoutePresenter> listRoutes(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return routeService.listRoutes(page, size);
    }

    @GetMapping(path = "/{routeId}")
    public RoutePresenter listById(@PathVariable String routeId) {
        return routeService.listRouteById(routeId);
    }

    @PostMapping(path = "/{routeId}/passengers/{passengerId}")
    public void addPassengerToRoute(@PathVariable String routeId, @PathVariable String passengerId) {
        routeService.addPassengerToRoute(passengerId, routeId);
    }

    @GetMapping(path = "/{routeId}/passengers")
    public List<PassengerPresenter> listPassengersFromRoute(@PathVariable String routeId) {
        return passengerService.listPassengersFromRoute(routeId);
    }

    @DeleteMapping(path = "/{routeId}/passengers/{passengerId}")
    public void removePassengerFromRoute(@PathVariable String routeId, @PathVariable String passengerId) {
        routeService.removePassengerFromRoute(passengerId, routeId);
    }

    @PostMapping(path = "/{routeId}/enable")
    public void enableRoute(@PathVariable String routeId) {
        routeService.enableRoute(routeId);
    }

    @PostMapping(path = "/{routeId}/disable")
    public void disableRoute(@PathVariable String routeId) {
        routeService.disableRoute(routeId);
    }

}
