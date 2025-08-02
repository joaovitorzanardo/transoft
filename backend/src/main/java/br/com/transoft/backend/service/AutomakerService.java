package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.vehicle.presenter.AutomakerPresenter;
import br.com.transoft.backend.entity.Automaker;
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

    public void registerAutomaker(String name) {
        this.automakerRepository.save(new Automaker(name));
    }

    public List<AutomakerPresenter> listAutomakers() {
        return this.automakerRepository.findAll().stream().map(auto -> new AutomakerPresenter(auto.getAutomakerId(), auto.getName())).collect(Collectors.toList());
    }

}
