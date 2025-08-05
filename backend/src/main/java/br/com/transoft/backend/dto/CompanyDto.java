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
public class CompanyDto {

    @JsonProperty(namespace = "name")
    @NotBlank(message = "The company name must be informed.")
    private String name;

    @JsonProperty(namespace = "email")
    @NotBlank(message = "The company email must be informed.")
    private String email;

    @JsonProperty(namespace = "name")
    @NotBlank(message = "The company CNPJ must be informed.")
    private String cnpj;

}
