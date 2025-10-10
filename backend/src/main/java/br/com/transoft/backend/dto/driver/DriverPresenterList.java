package br.com.transoft.backend.dto.driver;

import java.util.List;

public record DriverPresenterList(int count, List<DriverPresenter> drivers) {
}
