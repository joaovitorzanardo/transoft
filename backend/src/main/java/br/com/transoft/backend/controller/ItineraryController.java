package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.PassengerItineraryPresenter;
import br.com.transoft.backend.dto.itinerary.ItineraryDto;
import br.com.transoft.backend.dto.itinerary.ItineraryPresenter;
import br.com.transoft.backend.service.ItineraryService;
import jakarta.validation.Valid;
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
    public void generateItinerary(@Valid @RequestBody ItineraryDto itineraryDto, Authentication authentication) {
        itineraryService.generateItinerary(itineraryDto, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping
    public List<ItineraryPresenter> listItineraries(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, Authentication authentication) {
        return itineraryService.listItineraries(page, size, (LoggedUserAccount) authentication.getPrincipal());
    }

    @GetMapping("/{itineraryId}")
    public ItineraryPresenter listItineraryById(@PathVariable String itineraryId, Authentication authentication) {
        return itineraryService.findItineraryById(itineraryId, (LoggedUserAccount) authentication.getPrincipal()).toPresenter();
    }

    @GetMapping("/{itineraryId}/passengers")
    public List<PassengerItineraryPresenter> listPassengersFromItinerary(@PathVariable String itineraryId, Authentication authentication) {
        return itineraryService.listPassengersFromItinerary(itineraryId, (LoggedUserAccount) authentication.getPrincipal());
    }
}
