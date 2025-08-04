package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.vehicle.presenter.VehicleModelPresenter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle_model")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VehicleModel {

    @Id
    @Column(name = "vehicle_model_id")
    private String vehicleModelId;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "model_year", nullable = false)
    private Integer modelYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "automaker_id")
    private Automaker automaker;

    public VehicleModelPresenter toPresenter() {
        return new VehicleModelPresenter(
                vehicleModelId,
                modelName,
                modelYear,
                automaker.toPresenter()
        );
    }


}
