package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.vehicle.VehicleDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;
import br.com.transoft.backend.entity.Company;
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

    public void saveVehicle(VehicleDto vehicleDto, LoggedUserAccount loggedUserAccount) {
        VehicleModel vehicleModel = vehicleModelService.findVehicleModelById(vehicleDto.getVehicleModelId());

        if (plateNumberRegistered(vehicleDto.getPlateNumber(), loggedUserAccount)) {
            throw new ResourceConflictException("PlateNumber already registered");
        }

        Vehicle vehicle = Vehicle.builder()
                .vehicleId(UUID.randomUUID().toString())
                .plateNumber(vehicleDto.getPlateNumber())
                .capacity(vehicleDto.getCapacity())
                .vehicleModel(vehicleModel)
                .active(true)
                .company(new Company(loggedUserAccount.companyId()))
                .build();

        vehicleRepository.save(vehicle);
    }

    public List<VehiclePresenter> listVehicles(int page, int size, LoggedUserAccount loggedUserAccount) {
        return vehicleRepository.findAllByCompany_CompanyId(loggedUserAccount.companyId(), PageRequest.of(page, size)).stream().map(Vehicle::toPresenter).toList();
    }

    public Vehicle findVehicleById(String vehicleId, LoggedUserAccount loggedUserAccount) {
        return vehicleRepository.findByVehicleIdAndCompany_CompanyId(vehicleId, loggedUserAccount.companyId()).orElseThrow(() -> new ResourceNotFoundException("Vehicle id was not found"));
    }

    public void updateVehicle(String vehicleId, VehicleDto vehicleDto, LoggedUserAccount loggedUserAccount) {
        Vehicle vehicle = findVehicleById(vehicleId, loggedUserAccount);

        if (!vehicleDto.getPlateNumber().equals(vehicle.getPlateNumber())) {
            if (plateNumberRegistered(vehicleDto.getPlateNumber(), loggedUserAccount)) {
                throw new ResourceConflictException("Plate number already registered");
            }

            vehicle.setPlateNumber(vehicleDto.getPlateNumber());
        }

        vehicle.setCapacity(vehicleDto.getCapacity());

        vehicleRepository.save(vehicle);
    }

    public boolean plateNumberRegistered(String plateNumber, LoggedUserAccount loggedUserAccount) {
        return vehicleRepository.findByPlateNumberAndCompany_CompanyId(plateNumber, loggedUserAccount.companyId()).isPresent();
    }

    public void enableVehicle(String vehicleId, LoggedUserAccount loggedUserAccount) {
        Vehicle vehicle = findVehicleById(vehicleId, loggedUserAccount);
        vehicle.enable();
        vehicleRepository.save(vehicle);
    }

    public void disableVehicle(String vehicleId, LoggedUserAccount loggedUserAccount) {
        Vehicle vehicle = findVehicleById(vehicleId, loggedUserAccount);
        vehicle.disable();
        vehicleRepository.save(vehicle);
    }

}
