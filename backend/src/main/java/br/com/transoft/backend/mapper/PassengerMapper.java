package br.com.transoft.backend.mapper;

import br.com.transoft.backend.dto.passenger.PassengerDto;
import br.com.transoft.backend.entity.*;
import br.com.transoft.backend.entity.route.Route;

import java.util.UUID;

public class PassengerMapper {

    public static Passenger toEntity(PassengerDto passengerDto, Address address, Route route, UserAccount userAccount, Company company) {
        return Passenger.builder()
                .passengerId(UUID.randomUUID().toString())
                .name(passengerDto.getName())
                .email(passengerDto.getEmail())
                .phoneNumber(passengerDto.getPhoneNumber().toEntity())
                .route(route)
                .address(address)
                .userAccount(userAccount)
                .company(company)
                .build();
    }

}
