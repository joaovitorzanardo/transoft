package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.PhoneNumberDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PhoneNumber {

    @Column(name = "phone_ddd", nullable = false)
    private String ddd;

    @Column(name = "phone_number", nullable = false)
    private String number;

    public PhoneNumber(PhoneNumberDto phoneNumberDto) {
        this.ddd = phoneNumberDto.getDdd();
        this.number = phoneNumberDto.getNumber();
    }

}
