package br.com.transoft.backend.dto.passenger;

import br.com.transoft.backend.dto.PhoneNumberDto;
import br.com.transoft.backend.dto.address.AddressDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PassengerPresenter(
        @JsonProperty(namespace = "passengerId") String passengerId,
        @JsonProperty(namespace = "name") String name,
        @JsonProperty(namespace = "email") String email,
        @JsonProperty(namespace = "phoneNumber") PhoneNumberDto phoneNumber,
        @JsonProperty(namespace = "routeId") String routeId,
        @JsonProperty(namespace = "routeName") String routeName,
        @JsonProperty(namespace = "address") AddressDto address,
        @JsonProperty(namespace = "active") Boolean active,
        @JsonProperty(namespace = "enabled") Boolean enabled) {
}
