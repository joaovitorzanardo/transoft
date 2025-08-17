package br.com.transoft.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateIntervalDto {

    @JsonProperty(namespace = "start_date")
    @NotNull(message = "The start date must be informed.")
    private LocalDate startDate;

    @JsonProperty(namespace = "end_date")
    @NotNull(message = "The end date must be informed.")
    private LocalDate endDate;

}
