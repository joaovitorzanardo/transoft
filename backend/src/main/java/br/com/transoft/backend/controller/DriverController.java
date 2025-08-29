package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.driver.DriverDto;
import br.com.transoft.backend.dto.driver.DriverPresenter;
import br.com.transoft.backend.dto.driver.account.DriverAccountDto;
import br.com.transoft.backend.dto.driver.account.DriverAccountPresenter;
import br.com.transoft.backend.service.DriverService;
import jakarta.validation.Valid;
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
    public void saveDriver(@Valid @RequestBody DriverDto driverDto) {
        driverService.saveDriver(driverDto);
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
    public List<DriverPresenter> listDrivers(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return driverService.listDrivers(page, size);
    }

    @GetMapping(path = "/{driverId}")
    public DriverPresenter getDriverById(@PathVariable String driverId) {
        return driverService.findDriverById(driverId).toPresenter();
    }

    @PutMapping(path = "/{driverId}")
    public void updateDriver(@PathVariable String driverId, @RequestBody DriverDto driverDto) {
        driverService.updateDriver(driverId, driverDto);
    }

}
