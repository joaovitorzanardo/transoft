package br.com.transoft.backend.dto.driver;

import br.com.transoft.backend.dto.PhoneNumberDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record DriverPresenter(
        @JsonProperty(namespace = "driver_id") String driverId,
        @JsonProperty(namespace = "name") String name,
        @JsonProperty(namespace = "email") String email,
        @JsonProperty(namespace = "cnh_number") String cnhNumber,
        @JsonProperty(namespace = "cnh_expiration_date") LocalDate cnhExpirationDate,
        @JsonProperty(namespace = "phone_number") PhoneNumberDto phoneNumber
) {
}
