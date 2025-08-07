package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.vehicle.VehicleModelDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehicleModelPresenter;
import br.com.transoft.backend.service.VehicleModelService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/vehicle-model")
public class VehicleModelController {

    private final VehicleModelService vehicleModelService;

    public VehicleModelController(VehicleModelService vehicleModelService) {
        this.vehicleModelService = vehicleModelService;
    }

    @PostMapping
    public void saveVehicleModel(@Valid @RequestBody VehicleModelDto vehicleModelDto) {
        this.vehicleModelService.saveVehicleModel(vehicleModelDto);
    }

    @GetMapping
    public List<VehicleModelPresenter> listVehicleModelsByAutomaker(@RequestParam String automakerId) {
        return this.vehicleModelService.listVehicleModelsByAutomaker(automakerId);
    }

}
