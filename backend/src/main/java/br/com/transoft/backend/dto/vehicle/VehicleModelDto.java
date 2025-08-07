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
public class VehicleModelDto {

    @JsonProperty(namespace = "model_name")
    @NotBlank(message = "The name of the model must be informed.")
    private String modelName;

    @JsonProperty(namespace = "model_year")
    @NotNull(message = "The year of the model must be informed.")
    private Integer modelYear;

    @JsonProperty(namespace = "automaker_id")
    @NotNull(message = "The id of the automaker must be informed.")
    private String automakerId;

}
