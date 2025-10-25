package br.com.transoft.backend.dto.itinerary;

import java.time.LocalDate;
import java.util.List;

public record ItineraryFilter(List<String> status, List<String> type, LocalDate date) {
}
