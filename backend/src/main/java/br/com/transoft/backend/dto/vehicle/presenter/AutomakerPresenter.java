package br.com.transoft.backend.dto.vehicle.presenter;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AutomakerPresenter(@JsonProperty(namespace = "automaker_id") String automakerId,
                                 @JsonProperty(namespace = "name") String name) {
}
