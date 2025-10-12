package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.passenger.PassengerPresenter;
import br.com.transoft.backend.entity.route.Route;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "passenger")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Passenger {

    @Id
    @Column(name = "passenger_id")
    private String passengerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Embedded
    private PhoneNumber phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @Embedded
    private Address address;

    public PassengerPresenter toPresenter() {
        return new PassengerPresenter(
                passengerId,
                name,
                email,
                phoneNumber.toDto(),
                route.getRouteId(),
                route.getName(),
                address.toDto(),
                userAccount.getActive(),
                userAccount.getEnabled()
        );
    }

}
