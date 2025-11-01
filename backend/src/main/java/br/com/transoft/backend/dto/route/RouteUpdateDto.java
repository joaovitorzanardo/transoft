package br.com.transoft.backend.dto.route;

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
public class RouteUpdateDto {

    @JsonProperty(namespace = "name")
    @NotBlank(message = "Route name must be informed.")
    private String name;

    @JsonProperty(namespace = "defaultDriverId")
    @NotBlank(message = "Default driver id must be informed.")
    private String defaultDriverId;

    @JsonProperty(namespace = "defaultVehicleId")
    @NotBlank(message = "Default vehicle id must be informed.")
    private String defaultVehicleId;

    @JsonProperty(namespace = "departureTrip")
    @NotNull(message = "Departure trip info must be informed.")
    private DepartureTripDto departureTrip;

    @JsonProperty(namespace = "returnTrip")
    @NotNull(message = "Return trip info must be informed.")
    private ReturnTripDto returnTrip;

    @JsonProperty(namespace = "daysOfWeek")
    @NotNull(message = "Days of the week must be informed.")
    private DaysOfWeekDto daysOfWeek;

    @JsonProperty(namespace = "updateItineraries")
    private boolean updateItineraries = false;

}
