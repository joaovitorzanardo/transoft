package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.vehicle.VehicleDto;
import br.com.transoft.backend.entity.Vehicle;
import br.com.transoft.backend.entity.VehicleModel;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.VehicleModelRepository;
import br.com.transoft.backend.repository.VehicleRepository;
import org.springframework.stereotype.Service;

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

        boolean isPlateNumberRegistered = this.vehicleRepository.findByPlateNumber(vehicleDto.getPlateNumber()).isPresent();

        if (isPlateNumberRegistered) {
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


}
