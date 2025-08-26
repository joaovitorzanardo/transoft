package br.com.transoft.backend.dto.itinerary;

import br.com.transoft.backend.dto.driver.DriverPresenter;
import br.com.transoft.backend.dto.route.RoutePresenter;
import br.com.transoft.backend.utils.ItineraryStatus;
import br.com.transoft.backend.utils.ItineraryType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record ItineraryPresenter(
        String itineraryId,
        ItineraryType type,
        ItineraryStatus status,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        RoutePresenter route,
        DriverPresenter driver,
        Set<ItineraryPassengerPresenter> passengers
) {}
