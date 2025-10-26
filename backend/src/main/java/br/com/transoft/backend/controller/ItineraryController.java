package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.itinerary.*;
import br.com.transoft.backend.dto.itinerary.account.ItineraryAccount;
import br.com.transoft.backend.service.ItineraryService;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.CREATED)
    public void generateItinerary(@Valid @RequestBody GenerateItineraryDto generateItineraryDto, Authentication authentication) {
        itineraryService.generateItinerary(generateItineraryDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PatchMapping(path = "/{itineraryId}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void updateItinerary(@PathVariable String itineraryId, @Valid @RequestBody ItineraryDto itineraryDto, Authentication authentication) {
        itineraryService.updateItinerary(itineraryId, itineraryDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PatchMapping(path = "/{itineraryId}/cancel")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void cancelItinerary(@PathVariable String itineraryId, Authentication authentication) {
        itineraryService.cancelItinerary(itineraryId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public ItineraryPresenterList listItineraries(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, Authentication authentication) {
        return itineraryService.listItineraries(page, size, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/account")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public List<ItineraryAccount> listItinerariesByAccount(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, Authentication authentication) {
        return itineraryService.listItinerariesByAccount(page, size, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/history/account")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public List<ItineraryAccount> listItinerariesHistoryByAccount(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, Authentication authentication) {
        return itineraryService.listItinerariesHistoryByAccount(page, size, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PostMapping(path = "/filter")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public ItineraryPresenterList listItinerariesWithFilter(@RequestBody ItineraryFilter itineraryFilter, Authentication authentication) {
        return itineraryService.listItinerariesWithFilter(itineraryFilter, 0, 10, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping(path = "/stats")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public ItineraryStatsPresenter getItinerariesStats(Authentication authentication) {
        return itineraryService.getItinerariesStats((LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping("/{itineraryId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'DRIVER', 'PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public ItineraryPresenter listItineraryById(@PathVariable String itineraryId, Authentication authentication) {
        return itineraryService.findItineraryById(itineraryId, (LoggedUserAccount) authentication.getPrincipal()).toPresenter();
    }

    @GetMapping(path = "/next")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public ItineraryAccount getNextItinerary(Authentication authentication) {
        return itineraryService.getNextItinerary((LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping("/{itineraryId}/passengers")
    @PreAuthorize("hasAnyRole('MANAGER', 'DRIVER', 'PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public List<PassengerItineraryPresenter> listPassengersFromItinerary(@PathVariable String itineraryId, Authentication authentication) {
        return itineraryService.listPassengersFromItinerary(itineraryId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PatchMapping("/{itineraryId}/passenger/cancel")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void cancelItineraryForPassenger(@PathVariable String itineraryId, Authentication authentication) {
        itineraryService.cancelPassengerItinerary(itineraryId, (LoggedUserAccount) authentication.getPrincipal());
    }

    @PatchMapping("/{itineraryId}/passenger/confirm")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void confirmItineraryForPassenger(@PathVariable String itineraryId, Authentication authentication) {
        itineraryService.confirmPassengerItinerary(itineraryId, (LoggedUserAccount) authentication.getPrincipal());
    }

}
