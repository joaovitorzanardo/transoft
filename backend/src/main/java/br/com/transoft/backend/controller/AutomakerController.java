package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.vehicle.presenter.AutomakerPresenter;
import br.com.transoft.backend.service.AutomakerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/automaker")
public class AutomakerController {

    private final AutomakerService automakerService;

    public AutomakerController(AutomakerService automakerService) {
        this.automakerService = automakerService;
    }

    @PostMapping
    public void registerAutomaker(@RequestBody String name) {
        automakerService.registerAutomaker(name);
    }

    @GetMapping
    public List<AutomakerPresenter> listAutomaker() {
        return automakerService.listAutomakers();
    }

}
