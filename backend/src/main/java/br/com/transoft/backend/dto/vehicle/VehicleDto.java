package br.com.transoft.backend.dto.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleDto {

    @NotBlank(message = "The plate number must of the vehicle be informed.")
    private String plateNumber;

    @NotNull(message = "The vehicle capacity be informed.")
    private Integer capacity;

    @NotNull(message = "The vehicle model id be informed.")
    private String vehicleModelId;

    public VehicleDto(String plateNumber, Integer capacity, String vehicleModelId) {
        this.plateNumber = plateNumber;
        this.capacity = capacity;
        this.vehicleModelId = vehicleModelId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getVehicleModelId() {
        return vehicleModelId;
    }

    public void setVehicleModelId(String vehicleModelId) {
        this.vehicleModelId = vehicleModelId;
    }

}
