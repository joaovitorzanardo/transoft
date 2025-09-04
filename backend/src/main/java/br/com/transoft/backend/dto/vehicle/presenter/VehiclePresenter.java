package br.com.transoft.backend.dto.vehicle.presenter;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VehiclePresenter(
        @JsonProperty("vehicle_id") String vehicleId,
        @JsonProperty("plate_number") String plateNumber,
        @JsonProperty("capacity") Integer capacity,
        @JsonProperty("isActive") Boolean isActive,
        @JsonProperty("vehicle_model") VehicleModelPresenter vehicleModel)
{}
