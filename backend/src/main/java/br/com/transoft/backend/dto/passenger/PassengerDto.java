package br.com.transoft.backend.dto.passenger;

import br.com.transoft.backend.dto.PhoneNumberDto;
import br.com.transoft.backend.dto.address.AddressDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
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
public class PassengerDto {

    @JsonProperty(namespace = "name")
    @NotBlank(message = "The name must be informed.")
    private String name;

    @JsonProperty(namespace = "email")
    @Email(message = "The email is not in the right format")
    @NotBlank(message = "The email must be informed.")
    private String email;

    @JsonProperty(namespace = "phoneNumber")
    @NotNull(message = "The phone number must be informed.")
    private PhoneNumberDto phoneNumber;

    @JsonProperty(namespace = "routeId")
    @NotBlank(message = "The route id must be informed.")
    private String routeId;

    @JsonProperty(namespace = "address")
    @NotNull(message = "The address must be informed.")
    private AddressDto address;

}
