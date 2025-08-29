package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.vehicle.VehicleModelDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehicleModelPresenter;
import br.com.transoft.backend.service.VehicleModelService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/vehicle-models")
public class VehicleModelController {

    private final VehicleModelService vehicleModelService;

    public VehicleModelController(VehicleModelService vehicleModelService) {
        this.vehicleModelService = vehicleModelService;
    }

    @PostMapping
    public void saveVehicleModel(@Valid @RequestBody VehicleModelDto vehicleModelDto) {
        vehicleModelService.saveVehicleModel(vehicleModelDto);
    }

    @GetMapping
    public List<VehicleModelPresenter> listVehicleModels(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return vehicleModelService.listVehicleModels(page, size);
    }

    @GetMapping(path = "/automaker/{automakerId}")
    public List<VehicleModelPresenter> listVehicleModelsByAutomaker(@PathVariable String automakerId) {
        return vehicleModelService.listVehicleModelsByAutomaker(automakerId);
    }

    @GetMapping(path = "/{vehicleModelId}")
    public VehicleModelPresenter findVehicleModelById(@PathVariable String vehicleModelId) {
        return vehicleModelService.findVehicleModelById(vehicleModelId).toPresenter();
    }

}
