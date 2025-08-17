package br.com.transoft.backend.service;

import br.com.transoft.backend.utils.PasswordGeneratorUtils;
import br.com.transoft.backend.dto.passenger.PassengerDto;
import br.com.transoft.backend.dto.passenger.PassengerPresenter;
import br.com.transoft.backend.entity.*;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.PassengerRepository;
import br.com.transoft.backend.repository.SchoolRepository;
import org.passay.PasswordGenerator;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final SchoolRepository schoolRepository;
    private final KeycloakService keycloakService;
    private final PasswordGenerator passwordGenerator;

    public PassengerService(PassengerRepository passengerRepository, SchoolRepository schoolRepository, KeycloakService keycloakService) {
        this.passengerRepository = passengerRepository;
        this.schoolRepository = schoolRepository;
        this.keycloakService = keycloakService;
        this.passwordGenerator = new PasswordGenerator();
    }

    public void savePassenger(PassengerDto passengerDto) {
        // TODO: get company from JWT and set to the user
        if (isEmailRegistered(passengerDto.getEmail())) {
            throw new ResourceConflictException("This email is already registered for another passenger");
        }

        // TODO: create mapper for this
        Address address = Address.builder()
                .cep(passengerDto.getAddress().getCep())
                .street(passengerDto.getAddress().getStreet())
                .district(passengerDto.getAddress().getDistrict())
                .number(passengerDto.getAddress().getNumber())
                .complement(passengerDto.getAddress().getComplement())
                // TODO: calculate coordinates of the new address if necessary
                .coordinate(new Coordinate("32423423", "789234789"))
                .build();

        String userId = this.keycloakService.createUser(passengerDto.getName(), passengerDto.getEmail(), passwordGenerator.generatePassword(PasswordGeneratorUtils.DEFAULT_SIZE, PasswordGeneratorUtils.getDefaultRules()), false, List.of("PASSENGER"));

        UserAccount userAccount = UserAccount.builder()
                .userAccountId(userId)
                .name(passengerDto.getName())
                .email(passengerDto.getEmail())
                .role("PASSENGER")
                .active(false)
                .build();

        School school = this.schoolRepository.findById(passengerDto.getSchoolId()).orElseThrow(() -> new ResourceNotFoundException("School was not found."));

        Passenger passenger = Passenger.builder()
                .passengerId(UUID.randomUUID().toString())
                .name(passengerDto.getName())
                .email(passengerDto.getEmail())
                .phoneNumber(new PhoneNumber(passengerDto.getPhoneNumber().getDdd(), passengerDto.getPhoneNumber().getNumber()))
                .school(school)
                .address(address)
                .userAccount(userAccount)
                .build();

        //TODO: send email to the user with a password

        this.passengerRepository.save(passenger);
    }

    public List<PassengerPresenter> listPassengers(int page, int size) {
        return this.passengerRepository.findAll(PageRequest.of(page, size)).stream().map(Passenger::toPresenter).collect(Collectors.toList());
    }

    public PassengerPresenter findPassengerById(String passengerId) {
        return this.passengerRepository.findById(passengerId).orElseThrow(() -> new ResourceNotFoundException("Passenger was not found.")).toPresenter();
    }

    public void updatePassenger(String passengerId, PassengerDto passengerDto) {
        Passenger passenger = this.passengerRepository.findById(passengerId).orElseThrow(() -> new ResourceNotFoundException("Passenger was not found."));

        passenger.setName(passengerDto.getName());

        if (!passenger.getEmail().equals(passengerDto.getEmail())) {
            if (isEmailRegistered(passengerDto.getEmail())) {
                throw new ResourceConflictException("Email is already in use.");
            }
            passenger.setEmail(passengerDto.getEmail());
        }

        passenger.setPhoneNumber(new PhoneNumber(passenger.getPhoneNumber().getDdd(), passenger.getPhoneNumber().getNumber()));

        if (!passenger.getSchool().getSchoolId().equals(passengerDto.getSchoolId())) {
            School newSchool = this.schoolRepository.findById(passengerDto.getSchoolId()).orElseThrow(() -> new ResourceNotFoundException("School was not found."));
            passenger.setSchool(newSchool);
        }

        // TODO: create mapper for this
        Address address = Address.builder()
                .cep(passengerDto.getAddress().getCep())
                .street(passengerDto.getAddress().getStreet())
                .district(passengerDto.getAddress().getDistrict())
                .number(passengerDto.getAddress().getNumber())
                .complement(passengerDto.getAddress().getComplement())
                // TODO: calculate coordinates of the new address if necessary
                .coordinate(new Coordinate("32423423", "789234789"))
                .build();

        passenger.setAddress(address);
        passengerRepository.save(passenger);
    }

    private boolean isEmailRegistered(String email) {
        return this.passengerRepository.findByEmail(email).isPresent();
    }

    public void enablePassenger(String passengerId) {

    }

    public void disablePassenger(String passengerId) {

    }

}
