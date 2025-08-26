package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.PassengerItineraryPresenter;
import br.com.transoft.backend.dto.passenger.PassengerPresenter;
import br.com.transoft.backend.service.ItineraryService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LiveItineraryController {

    private final ItineraryService itineraryService;

    public LiveItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @MessageMapping("/itineraries/{itineraryId}/status")
    @SendTo("/live/itineraries/status")
    public String itineraryStatus(@DestinationVariable String itineraryId, @Payload String newStatus) {
        return itineraryService.changeItineraryStatus(itineraryId, newStatus);
    }

    @MessageMapping("/itineraries/{itineraryId}/passengers/{passengerId}")
    @SendTo("/live/itineraries/passengers")
    public List<PassengerItineraryPresenter> itineraryPassengers(@DestinationVariable String itineraryId, @DestinationVariable String passengerId, @Payload String newStatus) {
        return itineraryService.changePassengerStatus(itineraryId, passengerId, newStatus);
    }

}

