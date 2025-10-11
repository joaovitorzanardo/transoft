package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.driver.DriverPresenter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "driver")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Driver {

    @Id
    @Column(name = "driver_id")
    private String driverId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cnh_number", nullable = false)
    private String cnhNumber;

    @Column(name = "cnh_expiration_date", nullable = false)
    private LocalDate cnhExpirationDate;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Embedded
    private PhoneNumber phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public DriverPresenter toPresenter() {
        return new DriverPresenter(
                driverId,
                name,
                email,
                cnhNumber,
                cnhExpirationDate,
                phoneNumber.toDto(),
                userAccount.getActive(),
                userAccount.getEnabled()
        );
    }

}
