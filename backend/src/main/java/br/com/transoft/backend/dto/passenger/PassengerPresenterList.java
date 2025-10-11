package br.com.transoft.backend.dto.passenger;

import java.util.List;

public record PassengerPresenterList(int count, List<PassengerPresenter> passengers) {
}
