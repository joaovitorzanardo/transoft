package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.passenger.PassengerPresenter;
import br.com.transoft.backend.dto.route.RouteDto;
import br.com.transoft.backend.dto.route.RoutePresenter;
import br.com.transoft.backend.service.PassengerService;
import br.com.transoft.backend.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
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
    public void saveRoute(@Valid @RequestBody RouteDto routeDto, Authentication authentication) {
        routeService.saveRoute(routeDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping
    public List<RoutePresenter> listRoutes(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, Authentication authentication) {
        return routeService.listRoutes(page, size, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/{routeId}")
    public RoutePresenter listById(@PathVariable String routeId, Authentication authentication) {
        return routeService.listRouteById(routeId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PostMapping(path = "/{routeId}/passengers/{passengerId}")
    public void addPassengerToRoute(@PathVariable String routeId, @PathVariable String passengerId, Authentication authentication) {
        routeService.addPassengerToRoute(passengerId, routeId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/{routeId}/passengers")
    public List<PassengerPresenter> listPassengersFromRoute(@PathVariable String routeId, Authentication authentication) {
        return passengerService.listPassengersFromRoute(routeId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @DeleteMapping(path = "/{routeId}/passengers/{passengerId}")
    public void removePassengerFromRoute(@PathVariable String routeId, @PathVariable String passengerId, Authentication authentication) {
        routeService.removePassengerFromRoute(passengerId, routeId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PostMapping(path = "/{routeId}/enable")
    public void enableRoute(@PathVariable String routeId, Authentication authentication) {
        routeService.enableRoute(routeId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PostMapping(path = "/{routeId}/disable")
    public void disableRoute(@PathVariable String routeId, Authentication authentication) {
        routeService.disableRoute(routeId, (LoggedUserAccount) authentication.getPrincipal());
    }

}
