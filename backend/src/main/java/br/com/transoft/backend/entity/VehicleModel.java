package br.com.transoft.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicle_model")
public class VehicleModel {

    @Id
    @Column(name = "vehicle_model_id")
    private String vehicleModelId;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "model_year")
    private Integer modelYear;

    private Automaker automaker;

    public VehicleModel() {
    }

    public VehicleModel(String vehicleModelId, String modelName, Integer modelYear, Automaker automaker) {
        this.vehicleModelId = vehicleModelId;
        this.modelName = modelName;
        this.modelYear = modelYear;
        this.automaker = automaker;
    }

    public String getVehicleModelId() {
        return vehicleModelId;
    }

    public void setVehicleModelId(String vehicleModelId) {
        this.vehicleModelId = vehicleModelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public Automaker getAutomaker() {
        return automaker;
    }

    public void setAutomaker(Automaker automaker) {
        this.automaker = automaker;
    }

}
