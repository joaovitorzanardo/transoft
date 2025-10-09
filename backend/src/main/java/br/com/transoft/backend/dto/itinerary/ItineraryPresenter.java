package br.com.transoft.backend.dto.itinerary;

import br.com.transoft.backend.dto.driver.DriverPresenter;
import br.com.transoft.backend.dto.route.RoutePresenter;
import br.com.transoft.backend.constants.ItineraryStatus;
import br.com.transoft.backend.constants.ItineraryType;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;

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
        VehiclePresenter vehicle,
        Set<ItineraryPassengerPresenter> passengers
) {}
