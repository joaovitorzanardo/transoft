package br.com.transoft.backend.mapper;

import br.com.transoft.backend.constants.Role;
import br.com.transoft.backend.dto.registration.RegistrationAdminDto;
import br.com.transoft.backend.dto.registration.RegistrationDto;
import br.com.transoft.backend.dto.driver.DriverDto;
import br.com.transoft.backend.dto.passenger.PassengerDto;
import br.com.transoft.backend.entity.Company;
import br.com.transoft.backend.entity.UserAccount;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UserAccountMapper {

    public static UserAccount toPassengerAccount(PassengerDto passengerDto, String password, Company company) {
        return UserAccount.builder()
                .userAccountId(UUID.randomUUID().toString())
                .name(passengerDto.getName())
                .email(passengerDto.getEmail())
                .password(password)
                .role(Role.PASSENGER)
                .company(company)
                .active(false)
                .build();
    }

    public static UserAccount toDriverAccount(DriverDto driverDto, String password, Company company) {
        return UserAccount.builder()
                .userAccountId(UUID.randomUUID().toString())
                .name(driverDto.getName())
                .email(driverDto.getEmail())
                .password(password)
                .role(Role.DRIVER)
                .company(company)
                .active(false)
                .build();
    }

    public static UserAccount toManagerAccount(RegistrationDto registrationDto, PasswordEncoder passwordEncoder, Company company) {
        return UserAccount.builder()
                .userAccountId(UUID.randomUUID().toString())
                .name(registrationDto.getName())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .role(Role.MANAGER)
                .company(company)
                .active(true)
                .build();
    }

    public static UserAccount toSysAdminAccount(RegistrationAdminDto registrationAdminDto, PasswordEncoder passwordEncoder) {
        return UserAccount.builder()
                .userAccountId(UUID.randomUUID().toString())
                .name(registrationAdminDto.getName())
                .email(registrationAdminDto.getEmail())
                .password(passwordEncoder.encode(registrationAdminDto.getPassword()))
                .role(Role.SYS_ADMIN)
                .active(true)
                .build();
    }

}
