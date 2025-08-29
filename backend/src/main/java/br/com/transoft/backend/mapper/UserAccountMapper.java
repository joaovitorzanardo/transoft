package br.com.transoft.backend.mapper;

import br.com.transoft.backend.constants.Role;
import br.com.transoft.backend.dto.RegistrationDto;
import br.com.transoft.backend.dto.driver.DriverDto;
import br.com.transoft.backend.dto.passenger.PassengerDto;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.utils.PasswordGeneratorUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UserAccountMapper {

    public static UserAccount toPassengerAccount(PassengerDto passengerDto, PasswordEncoder passwordEncoder) {
        return UserAccount.builder()
                .userAccountId(UUID.randomUUID().toString())
                .name(passengerDto.getName())
                .email(passengerDto.getEmail())
                .password(passwordEncoder.encode(PasswordGeneratorUtils.generatePassword()))
                .role(Role.PASSENGER)
                .active(false)
                .build();
    }

    public static UserAccount toDriverAccount(DriverDto driverDto, PasswordEncoder passwordEncoder) {
        return UserAccount.builder()
                .userAccountId(UUID.randomUUID().toString())
                .name(driverDto.getName())
                .email(driverDto.getEmail())
                .password(passwordEncoder.encode(PasswordGeneratorUtils.generatePassword()))
                .role(Role.PASSENGER)
                .active(false)
                .build();
    }

    public static UserAccount toManagerAccount(RegistrationDto registrationDto, PasswordEncoder passwordEncoder) {
        return UserAccount.builder()
                .userAccountId(UUID.randomUUID().toString())
                .name(registrationDto.getName())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .role(Role.MANAGER)
                .active(true)
                .build();
    }

}
