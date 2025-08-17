package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.route.RouteDto;
import br.com.transoft.backend.dto.route.RoutePresenter;
import br.com.transoft.backend.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/route")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public void saveRoute(@Valid @RequestBody RouteDto routeDto) {
        this.routeService.saveRoute(routeDto);
    }

    @GetMapping
    public List<RoutePresenter> listRoutes(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return this.routeService.listRoutes(page, size);
    }

    @GetMapping(path = "/{routeId}")
    public RoutePresenter listById(@PathVariable String routeId) {
        return this.routeService.listRouteById(routeId);
    }

    @PostMapping(path = "/{routeId}/enable")
    public void enableRoute(@PathVariable String routeId) {
        this.routeService.enableRoute(routeId);
    }

    @PostMapping(path = "/{routeId}/disable")
    public void disableRoute(@PathVariable String routeId) {
        this.routeService.disableRoute(routeId);
    }

}
