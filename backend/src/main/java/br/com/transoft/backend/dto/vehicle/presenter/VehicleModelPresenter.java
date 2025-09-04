package br.com.transoft.backend.dto.vehicle.presenter;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VehicleModelPresenter(
        @JsonProperty(namespace = "vehicle_model_id") String vehicleModelId,
        @JsonProperty(namespace = "model_name") String modelName,
        @JsonProperty(namespace = "model_year") Integer modelYear,
        @JsonProperty(namespace = "automaker") AutomakerPresenter automaker)
{}
