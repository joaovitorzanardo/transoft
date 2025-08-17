package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.driver.DriverDto;
import br.com.transoft.backend.dto.driver.DriverPresenter;
import br.com.transoft.backend.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public void saveDriver(@Valid @RequestBody DriverDto driverDto) {
        this.driverService.saveDriver(driverDto);
    }

    @GetMapping
    public List<DriverPresenter> listDrivers(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return this.driverService.listDrivers(page, size);
    }

    @GetMapping(path = "/{driverId}")
    public DriverPresenter getDriverById(@PathVariable String driverId) {
        return this.driverService.findDriverById(driverId);
    }

    @PutMapping(path = "/{driverId}")
    public void updateDriver(@PathVariable String driverId, @RequestBody DriverDto driverDto) {
        this.driverService.updateDriver(driverId, driverDto);
    }

}
