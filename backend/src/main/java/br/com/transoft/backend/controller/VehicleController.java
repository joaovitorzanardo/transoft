package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.vehicle.VehicleDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenterList;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclesStatsPresenter;
import br.com.transoft.backend.service.VehicleService;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerVehicle(@RequestBody @Valid VehicleDto vehicleDto, Authentication authentication) {
        vehicleService.saveVehicle(vehicleDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public VehiclePresenterList listVehicles(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, Authentication authentication) {
        return vehicleService.listVehicles(page, size, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/all")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public List<VehiclePresenter> listVehicles(Authentication authentication) {
        return vehicleService.listAllVehicles((LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/all/active")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public List<VehiclePresenter> listAllActiveVehicles(Authentication authentication) {
        return vehicleService.listAllActiveVehicles((LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public VehiclesStatsPresenter getVehiclesStats(Authentication authentication) {
        return vehicleService.getVehiclesStats((LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping("/{vehicleId}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public VehiclePresenter getVehiclesById(@PathVariable String vehicleId, Authentication authentication) {
        return vehicleService.findVehicleById(vehicleId, (LoggedUserAccount) authentication.getPrincipal()).toPresenter();
    }

    @PatchMapping("/{vehicleId}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void updateVehicle(@PathVariable String vehicleId, @Valid @RequestBody VehicleDto vehicleDto, Authentication authentication) {
        vehicleService.updateVehicle(vehicleId, vehicleDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PatchMapping("/{vehicleId}/enable")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void enableVehicle(@PathVariable String vehicleId, Authentication authentication) {
        vehicleService.enableVehicle(vehicleId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @DeleteMapping("/{vehicleId}/disable")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void disableVehicle(@PathVariable String vehicleId, Authentication authentication) {
        vehicleService.disableVehicle(vehicleId, (LoggedUserAccount) authentication.getPrincipal());
    }

}
