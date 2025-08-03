package br.com.transoft.backend.entity;

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

    @Column(name = "complement")
    private String complement;

    @Embedded
    private Coordinate coordinate;

}
