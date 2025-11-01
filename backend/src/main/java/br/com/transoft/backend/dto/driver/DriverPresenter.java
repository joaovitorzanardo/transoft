package br.com.transoft.backend.dto.driver;

import br.com.transoft.backend.dto.PhoneNumberDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record DriverPresenter(
        @JsonProperty(namespace = "driverId") String driverId,
        @JsonProperty(namespace = "name") String name,
        @JsonProperty(namespace = "email") String email,
        @JsonProperty(namespace = "cnhNumber") String cnhNumber,
        @JsonProperty(namespace = "cnhExpirationDate") String cnhExpirationDate,
        @JsonProperty(namespace = "phoneNumber") PhoneNumberDto phoneNumber,
        @JsonProperty(namespace = "active") Boolean active,
        @JsonProperty(namespace = "enabled") Boolean enabled
) {
}
