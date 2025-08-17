package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.itinerary.ItineraryDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/itinerary")
public class ItineraryController {

    @PostMapping
    public void generateItinerary(@Valid @RequestBody ItineraryDto itineraryDto) {

    }

}
