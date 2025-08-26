package br.com.transoft.backend.dto.driver.account;

import br.com.transoft.backend.dto.PhoneNumberDto;

public record DriverAccountPresenter(
        String driverId,
        String name,
        String email,
        PhoneNumberDto phoneNumber
) {}
