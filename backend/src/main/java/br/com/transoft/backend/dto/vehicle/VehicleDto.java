package br.com.transoft.backend.dto.vehicle;

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
public class VehicleDto {

    @JsonProperty(namespace = "plate_number")
    @NotBlank(message = "The plate number must of the vehicle be informed.")
    private String plateNumber;

    @JsonProperty(namespace = "capacity")
    @NotNull(message = "The vehicle capacity be informed.")
    private Integer capacity;

    @JsonProperty(namespace = "vehicle_model_id")
    @NotNull(message = "The vehicle model id be informed.")
    private String vehicleModelId;

}
