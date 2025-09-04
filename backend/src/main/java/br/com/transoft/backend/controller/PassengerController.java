package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.passenger.PassengerDto;
import br.com.transoft.backend.dto.passenger.PassengerPresenter;
import br.com.transoft.backend.dto.passenger.account.PassengerAccountDto;
import br.com.transoft.backend.service.PassengerService;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.CREATED)
    public void savePassenger(@Valid @RequestBody PassengerDto passengerDto, Authentication authentication) {
        passengerService.savePassenger(passengerDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PutMapping(path = "/account")
    @PreAuthorize("hasRole('PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassengerAccount(@Valid @RequestBody PassengerAccountDto passengerAccountDto) {
        passengerService.updatePassengerAccount(passengerAccountDto);
    }

    @GetMapping(path = "/account")
    @PreAuthorize("hasRole('PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public PassengerPresenter getPassengerAccount() {
        return passengerService.getPassengerAccount();
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public List<PassengerPresenter> listPassengers(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, Authentication authentication) {
        return passengerService.listPassengers(page, size, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/{passengerId}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public PassengerPresenter findPassengerById(@PathVariable String passengerId, Authentication authentication) {
        return passengerService.findPassengerById(passengerId, (LoggedUserAccount) authentication.getPrincipal()).toPresenter();
    }

    @PutMapping(path = "/{passengerId}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassenger(@PathVariable String passengerId, PassengerDto passengerDto, Authentication authentication) {
        passengerService.updatePassenger(passengerId, passengerDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PostMapping(path = "/{passengerId}/enable")
    @PreAuthorize("hasRole('PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void enablePassenger(@PathVariable String passengerId) {
        passengerService.enablePassenger(passengerId);
    }

    @PostMapping(path = "/{passengerId}/disable")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void disablePassenger(@PathVariable String passengerId) {
        passengerService.disablePassenger(passengerId);
    }

}
