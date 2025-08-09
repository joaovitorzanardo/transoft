package br.com.transoft.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

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

    @OneToMany
    private List<Trip> trips;

}
