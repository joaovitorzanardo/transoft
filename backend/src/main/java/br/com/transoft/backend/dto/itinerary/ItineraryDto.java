package br.com.transoft.backend.dto.itinerary;

import br.com.transoft.backend.dto.DateIntervalDto;
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
public class ItineraryDto {

    @JsonProperty(namespace = "route_id")
    @NotBlank(message = "The route id must be informed.")
    private String routeId;

    @JsonProperty(namespace = "date_interval")
    @NotNull(message = "The date interval must be informed.")
    private DateIntervalDto dateInterval;

}
