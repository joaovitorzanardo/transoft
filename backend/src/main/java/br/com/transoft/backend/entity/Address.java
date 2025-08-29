package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.address.AddressDto;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Address {

    @Column(name = "cep", nullable = false)
    private String cep;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "uf", nullable = false)
    private String uf;

    @Column(name = "complement")
    private String complement;

    @Embedded
    private Coordinate coordinate;

    public AddressDto toDto() {
        return AddressDto.builder()
                .cep(cep)
                .street(street)
                .district(district)
                .number(number)
                .complement(complement)
                .city(city)
                .uf(uf)
                .coordinate(coordinate.toDto())
                .build();
    }

    @Override
    public String toString() {
        return street + ", "
                + number + " - "
                + district + ", "
                + city + " - "
                + uf + ", "
                + cep;
    }
}
