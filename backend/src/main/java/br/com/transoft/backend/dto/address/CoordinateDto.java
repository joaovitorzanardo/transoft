package br.com.transoft.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Latitude must be informed.")
    private String latitude;

    @JsonProperty(namespace = "longitude")
    @NotBlank(message = "Longitude must be informed.")
    private String longitude;

}
