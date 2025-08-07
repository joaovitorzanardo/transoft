package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.vehicle.VehicleModelDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehicleModelPresenter;
import br.com.transoft.backend.entity.Automaker;
import br.com.transoft.backend.entity.VehicleModel;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.AutomakerRepository;
import br.com.transoft.backend.repository.VehicleModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public List<VehicleModelPresenter> listVehicleModelsByAutomaker(String automakerId) {
        Automaker automaker = this.automakerRepository.findById(automakerId).orElseThrow(() -> new ResourceNotFoundException("Automaker not found"));
        return this.vehicleModelRepository.findAllByAutomaker(automaker).stream().map(v -> new VehicleModelPresenter(v.getVehicleModelId(), v.getModelName(), v.getModelYear(), automaker.toPresenter())).toList();
    }

}
