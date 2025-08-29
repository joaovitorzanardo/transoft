package br.com.transoft.backend.dto;

import br.com.transoft.backend.entity.PhoneNumber;
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
public class PhoneNumberDto {

    @JsonProperty(namespace = "ddd")
    @NotBlank(message = "The ddd must be informed.")
    private String ddd;

    @JsonProperty(namespace = "ddd")
    @NotBlank(message = "The number must be informed.")
    private String number;

    public PhoneNumber toEntity() {
        return new PhoneNumber(ddd, number);
    }

}
