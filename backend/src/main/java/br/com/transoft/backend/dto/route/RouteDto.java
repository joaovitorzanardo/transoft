package br.com.transoft.backend.dto.route;

import br.com.transoft.backend.entity.route.Route;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto {

    @JsonProperty(namespace = "name")
    @NotBlank(message = "Route name must be informed.")
    private String name;

    @JsonProperty(namespace = "school_id")
    @NotBlank(message = "School id must be informed.")
    private String schoolId;

    @JsonProperty(namespace = "default_driver_id")
    @NotBlank(message = "Default driver id must be informed.")
    private String defaultDriverId;

    @JsonProperty(namespace = "departure_trip")
    @NotNull(message = "Departure trip info must be informed.")
    private DepartureTripDto departureTrip;

    @JsonProperty(namespace = "return_trip")
    @NotNull(message = "Return trip info must be informed.")
    private ReturnTripDto returnTrip;

    @JsonProperty(namespace = "day_of_week")
    @NotNull(message = "Days of the week must be informed.")
    private DaysOfWeekDto daysOfWeek;

}
