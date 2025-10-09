package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.vehicle.AutomakerDto;
import br.com.transoft.backend.dto.vehicle.presenter.AutomakerPresenter;
import br.com.transoft.backend.service.AutomakerService;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/automakers")
public class AutomakerController {

    private final AutomakerService automakerService;

    public AutomakerController(AutomakerService automakerService) {
        this.automakerService = automakerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SYS_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAutomaker(@RequestBody AutomakerDto automakerDto) {
        automakerService.registerAutomaker(automakerDto.getName());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public List<AutomakerPresenter> listAutomaker() {
        return automakerService.listAutomakers(); 
    }

}
