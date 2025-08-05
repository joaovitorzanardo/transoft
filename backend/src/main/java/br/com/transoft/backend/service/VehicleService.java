package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.vehicle.VehicleDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;
import br.com.transoft.backend.entity.Vehicle;
import br.com.transoft.backend.entity.VehicleModel;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.VehicleModelRepository;
import br.com.transoft.backend.repository.VehicleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleModelRepository vehicleModelRepository;

    public VehicleService(VehicleRepository vehicleRepository, VehicleModelRepository vehicleModelRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleModelRepository = vehicleModelRepository;
    }

    public void saveVehicle(VehicleDto vehicleDto) {
        //TODO: Get companyId from the JWT token and set on the vehicle object
        VehicleModel vehicleModel = this.vehicleModelRepository.findById(vehicleDto.getVehicleModelId()).orElseThrow(() -> new ResourceNotFoundException("No vehicle model was found with this id."));

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

        this.vehicleRepository.save(vehicle);
    }

    public List<VehiclePresenter> listVehicles(int page, int size) {
        return this.vehicleRepository.findAll(PageRequest.of(page, size)).stream().map(Vehicle::toPresenter).toList();
    }

    public VehiclePresenter findVehicleById(String vehicleId) {
        return this.vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle id was not found")).toPresenter();
    }

    public void updateVehicle(String vehicleId, VehicleDto vehicleDto) {
        Vehicle vehicle = this.vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle id was not found"));

        if (!vehicleDto.getPlateNumber().equals(vehicle.getPlateNumber())) {
            if (plateNumberRegistered(vehicleDto.getPlateNumber())) {
                throw new ResourceConflictException("PlateNumber already registered");
            }

            vehicle.setPlateNumber(vehicleDto.getPlateNumber());
        }

        vehicle.setCapacity(vehicleDto.getCapacity());

        this.vehicleRepository.save(vehicle);
    }

    public boolean plateNumberRegistered(String plateNumber) {
        return this.vehicleRepository.findByPlateNumber(plateNumber).isPresent();
    }

    public void enableVehicle(String vehicleId) {
        Vehicle vehicle = this.vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle id was not found"));
        vehicle.enable();
        this.vehicleRepository.save(vehicle);
    }

    public void disableVehicle(String vehicleId) {
        Vehicle vehicle = this.vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle id was not found"));
        vehicle.disable();
        this.vehicleRepository.save(vehicle);
    }


}
