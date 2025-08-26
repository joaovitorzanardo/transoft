package br.com.transoft.backend.entity.route;

import br.com.transoft.backend.dto.route.RoutePresenter;
import br.com.transoft.backend.entity.Company;
import br.com.transoft.backend.entity.Driver;
import br.com.transoft.backend.entity.Passenger;
import br.com.transoft.backend.entity.School;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "route")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Route {

    @Id
    @Column(name = "route_id")
    private String routeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver defaultDriver;

    @Embedded
    private DepartureTrip departureTrip;

    @Embedded
    private ReturnTrip returnTrip;

    @Embedded
    private DayOfWeek dayOfWeek;

    @OneToMany(mappedBy = "route")
    private Set<Passenger> passengers;

    public RoutePresenter toPresenter() {
        return new RoutePresenter(
                routeId,
                name,
                active,
                school.toPresenter(),
                defaultDriver.toPresenter(),
                departureTrip.toDto(),
                returnTrip.toDto(),
                dayOfWeek.toDto()
        );
    }

}
