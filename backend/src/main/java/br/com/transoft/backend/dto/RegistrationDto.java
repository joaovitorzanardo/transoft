package br.com.transoft.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegistrationDto {

    @Email
    @NotBlank(message = "The email must be informed.")
    private String email;

    @NotBlank(message = "The name must be informed.")
    private String name;

    @NotBlank(message = "The password must be informed.")
    private String password;

    @NotBlank(message = "The company CNPJ must be informed.")
    private String companyCnpj;

    @NotBlank(message = "The company name must be informed.")
    private String companyName;

    public RegistrationDto(String email, String name, String password, String companyCnpj, String companyName) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.companyCnpj = companyCnpj;
        this.companyName = companyName;
    }

}
