package br.com.transoft.backend.dto.vehicle;

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
public class AutomakerDto {

    @JsonProperty(namespace = "name")
    @NotBlank(message = "Automaker name must be informed.")
    private String name;

}
