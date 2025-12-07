package br.com.transoft.backend.tasks;

import br.com.transoft.backend.service.ItineraryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InvalidateMissedItinerariesTask {

    private final ItineraryService itineraryService;

    public InvalidateMissedItinerariesTask(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void invalidateFromToday() {
        itineraryService.invalidateMissedItinerariesFromToday();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void invalidateFromPreviousDays() {
        itineraryService.invalidateMissedItinerariesFromPreviousDays();
    }

}
