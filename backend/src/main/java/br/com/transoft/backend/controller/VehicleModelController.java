package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.vehicle.VehicleModelDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehicleModelPresenter;
import br.com.transoft.backend.service.VehicleModelService;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('SYS_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    public void saveVehicleModel(@Valid @RequestBody VehicleModelDto vehicleModelDto) {
        vehicleModelService.saveVehicleModel(vehicleModelDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'MANAGER')")
    @SecurityRequirement(name = "Authorization")
    public List<VehicleModelPresenter> listVehicleModels(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return vehicleModelService.listVehicleModels(page, size);
    }

    @GetMapping(path = "/automaker/{automakerId}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'MANAGER')")
    @SecurityRequirement(name = "Authorization")
    public List<VehicleModelPresenter> listVehicleModelsByAutomaker(@PathVariable String automakerId) {
        return vehicleModelService.listVehicleModelsByAutomaker(automakerId);
    }

    @GetMapping(path = "/{vehicleModelId}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'MANAGER')")
    @SecurityRequirement(name = "Authorization")
    public VehicleModelPresenter findVehicleModelById(@PathVariable String vehicleModelId) {
        return vehicleModelService.findVehicleModelById(vehicleModelId).toPresenter();
    }

}
