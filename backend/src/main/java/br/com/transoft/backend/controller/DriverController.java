package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.driver.DriverDto;
import br.com.transoft.backend.dto.driver.DriverPresenter;
import br.com.transoft.backend.dto.driver.account.DriverAccountDto;
import br.com.transoft.backend.dto.driver.account.DriverAccountPresenter;
import br.com.transoft.backend.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public void saveDriver(@Valid @RequestBody DriverDto driverDto, Authentication authentication) {
        driverService.saveDriver(driverDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PostMapping(path = "/account")
    public void updateDriverAccount(@Valid @RequestBody DriverAccountDto driverAccountDto) {
        driverService.updateDriverAccount(driverAccountDto);
    }

    @GetMapping(path = "/account")
    public DriverAccountPresenter getDriverAccount() {
        return this.driverService.getDriverAccount();
    }

    @GetMapping
    public List<DriverPresenter> listDrivers(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, Authentication authentication) {
        return driverService.listDrivers(page, size, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/{driverId}")
    public DriverPresenter getDriverById(@PathVariable String driverId, Authentication authentication) {
        return driverService.findDriverById(driverId, (LoggedUserAccount) authentication.getPrincipal()).toPresenter();
    }

    @PutMapping(path = "/{driverId}")
    public void updateDriver(@PathVariable String driverId, @RequestBody DriverDto driverDto, Authentication authentication) {
        driverService.updateDriver(driverId, driverDto, (LoggedUserAccount) authentication.getPrincipal());
    }

}
