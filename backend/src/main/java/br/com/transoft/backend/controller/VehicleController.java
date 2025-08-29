package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.vehicle.VehicleDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;
import br.com.transoft.backend.service.VehicleService;
import jakarta.validation.Valid;
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
    public void registerVehicle(@RequestBody @Valid VehicleDto vehicleDto) {
        vehicleService.saveVehicle(vehicleDto);
    }

    @GetMapping
    public List<VehiclePresenter> listVehicles(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return vehicleService.listVehicles(page, size);
    }

    @GetMapping("/{vehicleId}")
    public VehiclePresenter getVehiclesById(@PathVariable String vehicleId) {
        return vehicleService.findVehicleById(vehicleId).toPresenter();
    }

    @PutMapping("/{vehicleId}")
    public void updateVehicle(@PathVariable String vehicleId, @Valid @RequestBody VehicleDto vehicleDto) {
        vehicleService.updateVehicle(vehicleId, vehicleDto);
    }

    @PostMapping("/{vehicleId}/enable")
    public void enableVehicle(@PathVariable String vehicleId) {
        vehicleService.enableVehicle(vehicleId);
    }

    @PostMapping("/{vehicleId}/disable")
    public void disableVehicle(@PathVariable String vehicleId) {
        vehicleService.disableVehicle(vehicleId);
    }

}
