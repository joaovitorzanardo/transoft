package br.com.transoft.backend.dto.itinerary.account;

import java.util.List;

public record ItineraryAccount(String date, List<ItineraryAccountView> itinerary) {
}
