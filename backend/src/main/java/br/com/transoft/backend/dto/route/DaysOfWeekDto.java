package br.com.transoft.backend.dto.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DaysOfWeekDto {

    @JsonProperty(namespace = "monday")
    @NotNull(message = "The value for monday must be informed.")
    private boolean monday;

    @JsonProperty(namespace = "tuesday")
    @NotNull(message = "The value for tuesday must be informed.")
    private boolean tuesday;

    @JsonProperty(namespace = "wednesday")
    @NotNull(message = "The value for wednesday must be informed.")
    private boolean wednesday;

    @JsonProperty(namespace = "thursday")
    @NotNull(message = "The value for thursday must be informed.")
    private boolean thursday;

    @JsonProperty(namespace = "friday")
    @NotNull(message = "The value for friday must be informed.")
    private boolean friday;

}
