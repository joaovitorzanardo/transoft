package br.com.transoft.backend.dto.route;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ReturnTripDto {

    @JsonProperty(namespace = "start_time")
    @NotNull(message = "The start time of the return trip must be informed.")
    private LocalTime startTime;

    @JsonProperty(namespace = "end_time")
    @NotNull(message = "The end time of the return trip must be informed.")
    private LocalTime endTime;

}
