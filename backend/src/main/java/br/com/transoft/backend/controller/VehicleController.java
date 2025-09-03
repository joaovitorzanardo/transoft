package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.vehicle.VehicleDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;
import br.com.transoft.backend.service.VehicleService;
import jakarta.validation.Valid;
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
    public void registerVehicle(@RequestBody @Valid VehicleDto vehicleDto, Authentication authentication) {
        vehicleService.saveVehicle(vehicleDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping
    public List<VehiclePresenter> listVehicles(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, Authentication authentication) {
        return vehicleService.listVehicles(page, size, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping("/{vehicleId}")
    public VehiclePresenter getVehiclesById(@PathVariable String vehicleId, Authentication authentication) {
        return vehicleService.findVehicleById(vehicleId, (LoggedUserAccount) authentication.getPrincipal()).toPresenter();
    }

    @PutMapping("/{vehicleId}")
    public void updateVehicle(@PathVariable String vehicleId, @Valid @RequestBody VehicleDto vehicleDto, Authentication authentication) {
        vehicleService.updateVehicle(vehicleId, vehicleDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PostMapping("/{vehicleId}/enable")
    public void enableVehicle(@PathVariable String vehicleId, Authentication authentication) {
        vehicleService.enableVehicle(vehicleId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PostMapping("/{vehicleId}/disable")
    public void disableVehicle(@PathVariable String vehicleId, Authentication authentication) {
        vehicleService.disableVehicle(vehicleId, (LoggedUserAccount) authentication.getPrincipal());
    }

}
