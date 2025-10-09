package br.com.transoft.backend.dto.vehicle.presenter;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VehiclePresenter(
        @JsonProperty("vehicleId") String vehicleId,
        @JsonProperty("plateNumber") String plateNumber,
        @JsonProperty("capacity") Integer capacity,
        @JsonProperty("isActive") Boolean isActive,
        @JsonProperty("vehicleModel") VehicleModelPresenter vehicleModel)
{}
