package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.address.CoordinateDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Coordinate {

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    public CoordinateDto toDto() {
        return new CoordinateDto(latitude, longitude);
    }

}
