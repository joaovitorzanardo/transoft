package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.passenger.PassengerPresenter;
import br.com.transoft.backend.dto.route.*;
import br.com.transoft.backend.service.PassengerService;
import br.com.transoft.backend.service.RouteService;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRoute(@Valid @RequestBody RouteDto routeDto, Authentication authentication) {
        routeService.saveRoute(routeDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PatchMapping(path = "/{routeId}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void updateRoute(@PathVariable String routeId, @Valid @RequestBody RouteUpdateDto routeUpdateDto, Authentication authentication) {
        routeService.updateRoute(routeId, routeUpdateDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public RoutePresenterList listRoutes(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, Authentication authentication) {
        return routeService.listRoutes(page, size, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/all")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public List<RouteSelectPresenter> listRoutes(Authentication authentication) {
        return routeService.listAllRoutes((LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/all/active")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public List<RouteSelectPresenter> listAllActiveRoutes(Authentication authentication) {
        return routeService.listAllActiveRoutes((LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/stats")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public RoutesStatsPresenter getRoutesStats(Authentication authentication) {
        return routeService.getRoutesStats((LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/{routeId}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public RoutePresenter listById(@PathVariable String routeId, Authentication authentication) {
        return routeService.listRouteById(routeId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/{routeId}/passengers")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public List<PassengerPresenter> listPassengersFromRoute(@PathVariable String routeId, Authentication authentication) {
        return passengerService.listPassengersFromRoute(routeId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PatchMapping(path = "/{routeId}/enable")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void enableRoute(@PathVariable String routeId, Authentication authentication) {
        routeService.enableRoute(routeId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PatchMapping(path = "/{routeId}/disable")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void disableRoute(@PathVariable String routeId, Authentication authentication) {
        routeService.disableRoute(routeId, (LoggedUserAccount) authentication.getPrincipal());
    }

}
