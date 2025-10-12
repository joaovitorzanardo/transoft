package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.itinerary.ItineraryPresenter;
import br.com.transoft.backend.entity.route.Route;
import br.com.transoft.backend.constants.ItineraryStatus;
import br.com.transoft.backend.constants.ItineraryType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "itinerary")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Itinerary {

    @Id
    @Column(name = "itinerary_id")
    private String itineraryId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ItineraryType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ItineraryStatus status;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "itinerary", fetch = FetchType.LAZY)
    private Set<PassengerStatus> passengerStatus;

    public ItineraryPresenter toPresenter() {
        return new ItineraryPresenter(
                itineraryId,
                type,
                status,
                date,
                startTime,
                endTime,
                route.toPresenter(),
                driver.toPresenter(),
                vehicle.toPresenter()
        );
    }
}
