package br.com.transoft.backend.entity.route;

import br.com.transoft.backend.dto.route.DaysOfWeekDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DayOfTheWeek {

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

    public List<DayOfWeek> getDaysOfWeek() {
        List<DayOfWeek> days = new ArrayList<>();

        if (monday) days.add(DayOfWeek.MONDAY);
        if (tuesday) days.add(DayOfWeek.TUESDAY);
        if (wednesday) days.add(DayOfWeek.WEDNESDAY);
        if (thursday) days.add(DayOfWeek.THURSDAY);
        if (friday) days.add(DayOfWeek.FRIDAY);

        return days;
    }

}
