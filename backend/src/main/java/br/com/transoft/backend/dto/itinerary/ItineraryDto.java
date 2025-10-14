package br.com.transoft.backend.dto.itinerary;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryDto {

    @JsonProperty(namespace = "driverId")
    @NotBlank(message = "Driver id must be informed.")
    private String driverId;

    @JsonProperty(namespace = "vehicleId")
    @NotBlank(message = "Vehicle id must be informed.")
    private String vehicleId;

    @JsonProperty(namespace = "startTime")
    @NotNull(message = "The start time of the trip must be informed.")
    private LocalTime startTime;

    @JsonProperty(namespace = "endTime")
    @NotNull(message = "The end time of the trip must be informed.")
    private LocalTime endTime;

}
