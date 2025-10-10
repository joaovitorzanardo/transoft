package br.com.transoft.backend.dto.vehicle.presenter;

import java.util.List;

public record VehiclePresenterList(int count, List<VehiclePresenter> vehicles) {
}
