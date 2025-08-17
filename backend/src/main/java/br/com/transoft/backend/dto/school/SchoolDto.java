package br.com.transoft.backend.dto.school;

import br.com.transoft.backend.dto.address.AddressDto;
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
public class SchoolDto {

    @JsonProperty(namespace = "name")
    @NotBlank(message = "School name must be informed.")
    private String name;

    @JsonProperty(namespace = "address")
    @NotNull(message = "The school address must be informed.")
    private AddressDto address;

}
