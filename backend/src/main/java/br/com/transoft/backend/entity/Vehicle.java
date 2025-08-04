package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Vehicle {

    @Id
    @Column(name = "vehicle_id")
    private String vehicleId;

    @Column(name = "plateNumber", nullable = false)
    private String plateNumber;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_model_id")
    private VehicleModel vehicleModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public void enable() {
        this.active = true;
    }

    public void disable() {
        this.active = false;
    }

    public VehiclePresenter toPresenter() {
        return new VehiclePresenter(
                vehicleId,
                plateNumber,
                active,
                vehicleModel.toPresenter()
        );
    }

}
