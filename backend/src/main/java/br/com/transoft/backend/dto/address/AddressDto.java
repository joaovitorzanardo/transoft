package br.com.transoft.backend.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    @JsonProperty(namespace = "cep")
    @NotBlank(message = "Address CEP must be informed.")
    private String cep;

    @JsonProperty(namespace = "street")
    @NotBlank(message = "Address street must be informed.")
    private String street;

    @JsonProperty(namespace = "district")
    @NotBlank(message = "Address district must be informed.")
    private String district;

    @JsonProperty(namespace = "number")
    @NotNull(message = "Address number must be informed.")
    private Integer number;

    @JsonProperty(namespace = "city")
    @NotBlank(message = "Address city must be informed.")
    private String city;

    @JsonProperty(namespace = "uf")
    @NotBlank(message = "Address UF must be informed.")
    private String uf;

    @JsonProperty(namespace = "complement")
    private String complement;

    @JsonProperty(namespace = "coordinate")
    private CoordinateDto coordinate;

}
