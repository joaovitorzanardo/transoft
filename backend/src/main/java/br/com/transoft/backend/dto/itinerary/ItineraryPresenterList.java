package br.com.transoft.backend.dto.itinerary;

import java.util.List;

public record ItineraryPresenterList(int count, List<ItineraryPresenter> itineraries) {
}
