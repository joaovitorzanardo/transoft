package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.PassengerItineraryPresenter;
import br.com.transoft.backend.dto.itinerary.ItineraryDto;
import br.com.transoft.backend.dto.itinerary.ItineraryPresenter;
import br.com.transoft.backend.service.ItineraryService;
import jakarta.validation.Valid;
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
    public void generateItinerary(@Valid @RequestBody ItineraryDto itineraryDto) {
        this.itineraryService.generateItinerary(itineraryDto);
    }

    @GetMapping
    public List<ItineraryPresenter> listItineraries(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return this.itineraryService.listItineraries(page, size);
    }

    @GetMapping("/{itineraryId}")
    public ItineraryPresenter listItineraryById(@PathVariable String itineraryId) {
        return this.itineraryService.listItineraryById(itineraryId);
    }

    @GetMapping("/{itineraryId}/passengers")
    public List<PassengerItineraryPresenter> listPassengersFromItinerary(@PathVariable String itineraryId) {
        return itineraryService.listPassengersFromItinerary(itineraryId);
    }
}
