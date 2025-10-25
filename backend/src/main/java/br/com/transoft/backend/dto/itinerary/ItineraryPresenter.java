package br.com.transoft.backend.dto.itinerary;

import br.com.transoft.backend.dto.driver.DriverPresenter;
import br.com.transoft.backend.dto.route.RoutePresenter;
import br.com.transoft.backend.constants.ItineraryStatus;
import br.com.transoft.backend.constants.ItineraryType;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;

public record ItineraryPresenter(
        String itineraryId,
        ItineraryType type,
        ItineraryStatus status,
        String date,
        String startTime,
        String endTime,
        RoutePresenter route,
        DriverPresenter driver,
        VehiclePresenter vehicle
) {}
