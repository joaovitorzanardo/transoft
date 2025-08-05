package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.driver.DriverDto;
import br.com.transoft.backend.service.DriverService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public void saveDriver(DriverDto driverDto) {
        this.driverService.saveDriver(driverDto);
    }

}
