package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.vehicle.presenter.VehicleModelPresenter;
import br.com.transoft.backend.entity.Automaker;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.AutomakerRepository;
import br.com.transoft.backend.repository.VehicleModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleModelService {

    private final AutomakerRepository automakerRepository;
    private final VehicleModelRepository vehicleModelRepository;

    public VehicleModelService(AutomakerRepository automakerRepository, VehicleModelRepository vehicleModelRepository) {
        this.automakerRepository = automakerRepository;
        this.vehicleModelRepository = vehicleModelRepository;
    }

    public List<VehicleModelPresenter> listVehicleModelsByAutomaker(String automakerId) {
        Automaker automaker = this.automakerRepository.findById(automakerId).orElseThrow(() -> new ResourceNotFoundException("Automaker not found"));
        return this.vehicleModelRepository.findAllByAutomaker(automaker).stream().map(v -> new VehicleModelPresenter(v.getVehicleModelId(), v.getModelName(), v.getModelYear(), automaker.toPresenter())).toList();
    }

}
