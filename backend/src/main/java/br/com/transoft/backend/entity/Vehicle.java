package br.com.transoft.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @Column(name = "vehicle_id")
    private String vehicleId;

    @Column(name = "plateNumber")
    private String plateNumber;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "capacity")
    private Integer capacity;

    private VehicleModel vehicleModel;

    public Vehicle() {
    }

    public Vehicle(String vehicleId, String plateNumber, Boolean active, Integer capacity, VehicleModel vehicleModel) {
        this.vehicleId = vehicleId;
        this.plateNumber = plateNumber;
        this.active = active;
        this.capacity = capacity;
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

}
