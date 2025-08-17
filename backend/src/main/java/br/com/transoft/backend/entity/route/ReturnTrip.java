package br.com.transoft.backend.entity.route;

import br.com.transoft.backend.dto.route.ReturnTripDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalTime;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReturnTrip {

    @Column(name = "return_start_time")
    private LocalTime startTime;

    @Column(name = "return_end_time")
    private LocalTime endTime;

    public ReturnTripDto toDto() {
        return new ReturnTripDto(startTime, endTime);
    }

}
