package br.com.transoft.backend.dto.address;

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
public class CoordinateDto {

    @JsonProperty(namespace = "latitude")
    @NotNull(message = "Latitude must be informed.")
    private Double latitude;

    @JsonProperty(namespace = "longitude")
    @NotNull(message = "Longitude must be informed.")
    private Double longitude;

}
