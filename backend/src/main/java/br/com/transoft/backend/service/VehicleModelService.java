package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.vehicle.VehicleModelDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehicleModelPresenter;
import br.com.transoft.backend.entity.Automaker;
import br.com.transoft.backend.entity.VehicleModel;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.AutomakerRepository;
import br.com.transoft.backend.repository.VehicleModelRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VehicleModelService {

    private final AutomakerRepository automakerRepository;
    private final VehicleModelRepository vehicleModelRepository;

    public VehicleModelService(AutomakerRepository automakerRepository, VehicleModelRepository vehicleModelRepository) {
        this.automakerRepository = automakerRepository;
        this.vehicleModelRepository = vehicleModelRepository;
    }

    public void saveVehicleModel(VehicleModelDto vehicleModelDto) {
        Automaker automaker = this.automakerRepository.findById(vehicleModelDto.getAutomakerId()).orElseThrow(() -> new ResourceNotFoundException("Automaker was no found."));

        VehicleModel vehicleModel = VehicleModel.builder()
                .vehicleModelId(UUID.randomUUID().toString())
                .modelName(vehicleModelDto.getModelName())
                .modelYear(vehicleModelDto.getModelYear())
                .automaker(automaker)
                .build();

        this.vehicleModelRepository.save(vehicleModel);
    }

    public List<VehicleModelPresenter> listVehicleModels(int page, int size) {
        return this.vehicleModelRepository.findAll(PageRequest.of(page, size)).stream().map(VehicleModel::toPresenter).collect(Collectors.toList());
    }

    public List<VehicleModelPresenter> listVehicleModelsByAutomaker(String automakerId, int page, int size) {
        Automaker automaker = this.automakerRepository.findById(automakerId).orElseThrow(() -> new ResourceNotFoundException("Automaker not found"));
        return this.vehicleModelRepository.findAllByAutomaker(automaker).stream().map(VehicleModel::toPresenter).toList();
    }

    public VehicleModelPresenter findVehicleModelById(String vehicleModelId) {
        return this.vehicleModelRepository.findById(vehicleModelId).orElseThrow(() -> new ResourceNotFoundException("Vehicle Model was not found")).toPresenter();
    }

}
