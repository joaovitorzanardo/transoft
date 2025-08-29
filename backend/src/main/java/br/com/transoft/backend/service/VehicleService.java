package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.vehicle.VehicleDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;
import br.com.transoft.backend.entity.Vehicle;
import br.com.transoft.backend.entity.VehicleModel;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.VehicleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleModelService vehicleModelService;

    public VehicleService(VehicleRepository vehicleRepository, VehicleModelService vehicleModelService) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleModelService = vehicleModelService;
    }

    public void saveVehicle(VehicleDto vehicleDto) {
        VehicleModel vehicleModel = vehicleModelService.findVehicleModelById(vehicleDto.getVehicleModelId());

        if (plateNumberRegistered(vehicleDto.getPlateNumber())) {
            throw new ResourceConflictException("PlateNumber already registered");
        }

        Vehicle vehicle = Vehicle.builder()
                .vehicleId(UUID.randomUUID().toString())
                .plateNumber(vehicleDto.getPlateNumber())
                .capacity(vehicleDto.getCapacity())
                .vehicleModel(vehicleModel)
                .active(true)
                .build();

        vehicleRepository.save(vehicle);
    }

    public List<VehiclePresenter> listVehicles(int page, int size) {
        return vehicleRepository.findAll(PageRequest.of(page, size)).stream().map(Vehicle::toPresenter).toList();
    }

    public Vehicle findVehicleById(String vehicleId) {
        return vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle id was not found"));
    }

    public void updateVehicle(String vehicleId, VehicleDto vehicleDto) {
        Vehicle vehicle = findVehicleById(vehicleId);

        if (!vehicleDto.getPlateNumber().equals(vehicle.getPlateNumber())) {
            if (plateNumberRegistered(vehicleDto.getPlateNumber())) {
                throw new ResourceConflictException("Plate number already registered");
            }

            vehicle.setPlateNumber(vehicleDto.getPlateNumber());
        }

        vehicle.setCapacity(vehicleDto.getCapacity());

        vehicleRepository.save(vehicle);
    }

    public boolean plateNumberRegistered(String plateNumber) {
        return vehicleRepository.findByPlateNumber(plateNumber).isPresent();
    }

    public void enableVehicle(String vehicleId) {
        Vehicle vehicle = findVehicleById(vehicleId);
        vehicle.enable();
        vehicleRepository.save(vehicle);
    }

    public void disableVehicle(String vehicleId) {
        Vehicle vehicle = findVehicleById(vehicleId);
        vehicle.disable();
        vehicleRepository.save(vehicle);
    }

}
