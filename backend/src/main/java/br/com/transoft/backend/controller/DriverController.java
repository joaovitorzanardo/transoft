package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.driver.DriverDto;
import br.com.transoft.backend.dto.driver.DriverPresenter;
import br.com.transoft.backend.dto.driver.DriverPresenterList;
import br.com.transoft.backend.dto.driver.DriverStatsPresenter;
import br.com.transoft.backend.dto.driver.account.DriverAccountDto;
import br.com.transoft.backend.dto.driver.account.DriverAccountPresenter;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclesStatsPresenter;
import br.com.transoft.backend.service.DriverService;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveDriver(@Valid @RequestBody DriverDto driverDto, Authentication authentication) {
        driverService.saveDriver(driverDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PostMapping(path = "/account")
    @PreAuthorize("hasRole('DRIVER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void updateDriverAccount(@Valid @RequestBody DriverAccountDto driverAccountDto) {
        driverService.updateDriverAccount(driverAccountDto);
    }

    @GetMapping(path = "/account")
    @PreAuthorize("hasRole('DRIVER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public DriverAccountPresenter getDriverAccount() {
        return this.driverService.getDriverAccount();
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public DriverPresenterList listDrivers(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, Authentication authentication) {
        return driverService.listDrivers(page, size, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public DriverStatsPresenter getDriversStats(Authentication authentication) {
        return driverService.getDriverStats((LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/{driverId}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public DriverPresenter getDriverById(@PathVariable String driverId, Authentication authentication) {
        return driverService.findDriverById(driverId, (LoggedUserAccount) authentication.getPrincipal()).toPresenter();
    }

    @PatchMapping(path = "/{driverId}/enable")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void enableDriver(@PathVariable String driverId, Authentication authentication) {
        driverService.enableDriver(driverId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PatchMapping(path = "/{driverId}/disable")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void disableDriver(@PathVariable String driverId, Authentication authentication) {
        driverService.disableDriver(driverId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PutMapping(path = "/{driverId}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void updateDriver(@PathVariable String driverId, @RequestBody DriverDto driverDto, Authentication authentication) {
        driverService.updateDriver(driverId, driverDto, (LoggedUserAccount) authentication.getPrincipal());
    }

}
