package br.com.transoft.backend.entity.route;

import br.com.transoft.backend.dto.route.DepartureTripDto;
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
public class DepartureTrip {

    @Column(name = "departure_start_time")
    private LocalTime startTime;

    @Column(name = "departure_end_time")
    private LocalTime endTime;

    public DepartureTripDto toDto() {
        return new DepartureTripDto(startTime, endTime);
    }

}
