package br.com.transoft.backend.entity.route;

import br.com.transoft.backend.dto.route.DaysOfWeekDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DayOfWeek {

    @Column(name = "monday")
    private Boolean monday;

    @Column(name = "tuesday")
    private Boolean tuesday;

    @Column(name = "wednesday")
    private Boolean wednesday;

    @Column(name = "thursday")
    private Boolean thursday;

    @Column(name = "friday")
    private Boolean friday;

    public DaysOfWeekDto toDto() {
        return new DaysOfWeekDto(monday, tuesday, wednesday, thursday, friday);
    }

}
