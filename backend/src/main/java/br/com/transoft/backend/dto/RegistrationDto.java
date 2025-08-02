package br.com.transoft.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegistrationDto {

    @Email
    @NotBlank(message = "The email must be informed.")
    private String email;

    @NotBlank(message = "The name must be informed.")
    private String name;

    @NotBlank(message = "The password must be informed.")
    private String password;

    @NotBlank(message = "The company cnpj must be informed.")
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyCnpj() {
        return companyCnpj;
    }

    public void setCompanyCnpj(String companyCnpj) {
        this.companyCnpj = companyCnpj;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
