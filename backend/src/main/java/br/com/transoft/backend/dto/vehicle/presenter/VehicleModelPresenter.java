package br.com.transoft.backend.dto.vehicle.presenter;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VehicleModelPresenter(@JsonProperty(namespace = "vehicle_model_id") String vehicleModelId,
                                    @JsonProperty(namespace = "automaker") AutomakerPresenter automaker,
                                    @JsonProperty(namespace = "model_year") Integer modelYear,
                                    @JsonProperty(namespace = "model_name") String modelName) {
}
