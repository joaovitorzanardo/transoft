package br.com.transoft.backend.dto.school;

import br.com.transoft.backend.dto.address.AddressDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SchoolPresenter(@JsonProperty(namespace = "school_id") String schoolId, String name, @JsonProperty(namespace = "address") AddressDto address) {
}
