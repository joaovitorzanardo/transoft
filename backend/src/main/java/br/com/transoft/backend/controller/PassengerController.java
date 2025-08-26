package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.passenger.PassengerDto;
import br.com.transoft.backend.dto.passenger.PassengerPresenter;
import br.com.transoft.backend.dto.passenger.account.PassengerAccountDto;
import br.com.transoft.backend.service.PassengerService;
import jakarta.validation.Valid;
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
    public void savePassenger(@Valid @RequestBody PassengerDto passengerDto) {
        this.passengerService.savePassenger(passengerDto);
    }

    @PostMapping(path = "/account")
    public void updatePassengerAccount(@Valid @RequestBody PassengerAccountDto passengerAccountDto) {
        this.passengerService.updatePassengerAccount(passengerAccountDto);
    }

    @GetMapping(path = "/account")
    public PassengerPresenter getPassengerAccount() {
        return this.passengerService.getPassengerAccount();
    }

    @GetMapping
    public List<PassengerPresenter> listPassengers(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return this.passengerService.listPassengers(page, size);
    }

    @GetMapping(path = "/{passengerId}")
    public PassengerPresenter findPassengerById(@PathVariable String passengerId) {
        return this.passengerService.findPassengerById(passengerId);
    }

    @PutMapping(path = "/{passengerId}")
    public void updatePassenger(@PathVariable String passengerId, PassengerDto passengerDto) {
        this.passengerService.updatePassenger(passengerId, passengerDto);
    }

    @PostMapping(path = "/{passengerId}/enable")
    public void enablePassenger(@PathVariable String passengerId) {
        this.passengerService.enablePassenger(passengerId);
    }

    @PostMapping(path = "/{passengerId}/disable")
    public void disablePassenger(@PathVariable String passengerId) {
        this.passengerService.disablePassenger(passengerId);
    }

}
