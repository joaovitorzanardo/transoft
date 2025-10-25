package br.com.transoft.backend.dto.itinerary.account;

public record ItineraryAccountView(
   String id,
   String routeName,
   String schoolName,
   String type,
   String status,
   String startTime,
   String endTime
) {}
