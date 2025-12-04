package br.com.transoft.backend.dto.itinerary;

public record ItineraryStatsPresenter(int total, int scheduled, int finished, int canceled, int missed) {
}
