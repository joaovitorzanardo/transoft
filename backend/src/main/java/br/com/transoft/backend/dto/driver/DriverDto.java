package br.com.transoft.backend.dto.driver;

import br.com.transoft.backend.dto.PhoneNumberDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto {

    @JsonProperty(namespace = "name")
    @NotBlank(message = "The driver name must be informed.")
    private String name;

    @JsonProperty(namespace = "cnh_number")
    @NotBlank(message = "The cnh number must be informed.")
    private String cnhNumber;

    @JsonProperty(namespace = "cnh_expiration_date")
    @NotBlank(message = "The cnh expiration date must be informed.")
    private LocalDate cnhExpirationDate;

    @JsonProperty(namespace = "email")
    @Email(message = "The driver email is not in the right format")
    @NotBlank(message = "The drive email must be informed.")
    private String email;

    @JsonProperty(namespace = "phone_number")
    @NotNull(message = "The phone number must be informed.")
    private PhoneNumberDto phoneNumber;

}
