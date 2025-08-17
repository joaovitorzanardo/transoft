package br.com.transoft.backend.dto.passenger;

import br.com.transoft.backend.dto.PhoneNumberDto;
import br.com.transoft.backend.dto.address.AddressDto;
import br.com.transoft.backend.dto.school.SchoolPresenter;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PassengerPresenter(
        @JsonProperty(namespace = "passenger_id") String passengerId,
        @JsonProperty(namespace = "name") String name,
        @JsonProperty(namespace = "email") String email,
        @JsonProperty(namespace = "phone_number") PhoneNumberDto phoneNumber,
        @JsonProperty(namespace = "school") SchoolPresenter school,
        @JsonProperty(namespace = "address") AddressDto address) {
}
