package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.passenger.PassengerPresenter;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Embedded
    private Address address;

    public PassengerPresenter toPresenter() {
        return new PassengerPresenter(
                passengerId,
                name,
                email,
                phoneNumber.toDto(),
                school.toPresenter(),
                address.toDto()
        );

    }

}
