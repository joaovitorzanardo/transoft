package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.vehicle.VehicleDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/vehicle")
public class VehicleController {

    @PostMapping
    public void registerVehicle(@RequestBody @Valid VehicleDto vehicleDto) {

    }

    @GetMapping
    public List<VehiclePresenter> listVehicles() {
        return null;
    }

    @GetMapping("/{vehicleId}")
    public List<VehiclePresenter> listVehicles(@PathVariable String vehicleId) {
        return null;
    }

    @PutMapping("/{vehicleId}")
    public void updateVehicle(@PathVariable String vehicleId, @Valid @RequestBody VehicleDto vehicleDto) {

    }

    @DeleteMapping("/{vehicleId}")
    public void disableVehicle(@PathVariable String vehicleId) {

    }

}
