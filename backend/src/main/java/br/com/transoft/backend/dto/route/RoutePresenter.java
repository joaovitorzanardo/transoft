package br.com.transoft.backend.dto.route;

import br.com.transoft.backend.dto.driver.DriverPresenter;
import br.com.transoft.backend.dto.school.SchoolPresenter;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;

public record RoutePresenter(
        String routeId,
        String name,
        Boolean active,
        SchoolPresenter school,
        DriverPresenter defaultDriver,
        VehiclePresenter defaultVehicle,
        DepartureTripDto departureTrip,
        ReturnTripDto returnTrip,
        DaysOfWeekDto daysOfWeek
) {}
