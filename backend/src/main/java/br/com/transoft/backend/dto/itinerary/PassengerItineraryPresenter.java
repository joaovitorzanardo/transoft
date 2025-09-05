package br.com.transoft.backend.dto;

import br.com.transoft.backend.dto.passenger.PassengerPresenter;

public record PassengerItineraryPresenter(
        PassengerPresenter passenger,
        String status
) {}
