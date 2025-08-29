package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.vehicle.VehicleModelDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehicleModelPresenter;
import br.com.transoft.backend.entity.Automaker;
import br.com.transoft.backend.entity.VehicleModel;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.VehicleModelRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;
    private final AutomakerService automakerService;

    public VehicleModelService(VehicleModelRepository vehicleModelRepository, AutomakerService automakerService) {
        this.vehicleModelRepository = vehicleModelRepository;
        this.automakerService = automakerService;
    }

    public void saveVehicleModel(VehicleModelDto vehicleModelDto) {
        Automaker automaker = automakerService.findByAutomakerById(vehicleModelDto.getAutomakerId());

        VehicleModel vehicleModel = VehicleModel.builder()
                .vehicleModelId(UUID.randomUUID().toString())
                .modelName(vehicleModelDto.getModelName())
                .modelYear(vehicleModelDto.getModelYear())
                .automaker(automaker)
                .build();

        vehicleModelRepository.save(vehicleModel);
    }

    public VehicleModel findVehicleModelById(String vehicleModelId) {
        return vehicleModelRepository.findById(vehicleModelId).orElseThrow(() -> new ResourceNotFoundException("Vehicle Model was not found"));
    }

    public List<VehicleModelPresenter> listVehicleModels(int page, int size) {
        return vehicleModelRepository.findAll(PageRequest.of(page, size)).stream().map(VehicleModel::toPresenter).collect(Collectors.toList());
    }

    public List<VehicleModelPresenter> listVehicleModelsByAutomaker(String automakerId) {
        Automaker automaker = automakerService.findByAutomakerById(automakerId);
        return vehicleModelRepository.findAllByAutomaker(automaker).stream().map(VehicleModel::toPresenter).toList();
    }

}
