package br.com.transoft.backend.dto.itinerary;

public record ItineraryPassengerPresenter (
        String passengerId,
        String name,
        String status
) {}
