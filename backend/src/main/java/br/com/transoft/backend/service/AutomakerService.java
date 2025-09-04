package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.vehicle.presenter.AutomakerPresenter;
import br.com.transoft.backend.entity.Automaker;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.AutomakerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutomakerService {

    private final AutomakerRepository automakerRepository;

    public AutomakerService(AutomakerRepository automakerRepository) {
        this.automakerRepository = automakerRepository;
    }

    public Automaker findByAutomakerById(String automakerId) {
        return automakerRepository.findById(automakerId).orElseThrow(() -> new ResourceNotFoundException("Automaker was no found."));
    }

    public void registerAutomaker(String name) {
        if (automakerExists(name)) {
            throw new ResourceConflictException("Automaker already exists.");
        }

        automakerRepository.save(new Automaker(name));
    }

    private boolean automakerExists(String name) {
        return automakerRepository.findByNameIgnoreCase(name).isPresent();
    }

    public List<AutomakerPresenter> listAutomakers() {
        return automakerRepository.findAll().stream().map(auto -> new AutomakerPresenter(auto.getAutomakerId(), auto.getName())).collect(Collectors.toList());
    }

}
