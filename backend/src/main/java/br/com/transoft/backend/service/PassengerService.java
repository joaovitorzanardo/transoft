package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.passenger.account.PassengerAccountDto;
import br.com.transoft.backend.utils.PasswordGeneratorUtils;
import br.com.transoft.backend.dto.passenger.PassengerDto;
import br.com.transoft.backend.dto.passenger.PassengerPresenter;
import br.com.transoft.backend.entity.*;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.PassengerRepository;
import br.com.transoft.backend.repository.SchoolRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.passay.PasswordGenerator;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final SchoolRepository schoolRepository;
    private final KeycloakService keycloakService;
    private final PasswordGenerator passwordGenerator;
    private final GeoApiContext geoApiContext;

    public PassengerService(PassengerRepository passengerRepository, SchoolRepository schoolRepository, KeycloakService keycloakService, GeoApiContext geoApiContext) {
        this.passengerRepository = passengerRepository;
        this.schoolRepository = schoolRepository;
        this.keycloakService = keycloakService;
        this.passwordGenerator = new PasswordGenerator();
        this.geoApiContext = geoApiContext;
    }

    public void savePassenger(PassengerDto passengerDto) {
        // TODO: get company from JWT and set to the user
        if (isEmailRegistered(passengerDto.getEmail())) {
            throw new ResourceConflictException("This email is already registered for another passenger");
        }

        Address address = Address.builder()
                .cep(passengerDto.getAddress().getCep())
                .street(passengerDto.getAddress().getStreet())
                .district(passengerDto.getAddress().getDistrict())
                .number(passengerDto.getAddress().getNumber())
                .complement(passengerDto.getAddress().getComplement())
                .city(passengerDto.getAddress().getCity())
                .uf(passengerDto.getAddress().getUf())
                .build();

        GeocodingApiRequest request = GeocodingApi
                .newRequest(geoApiContext)
                .address(address.toString());

        try {
            GeocodingResult result = request.await()[0];
            Coordinate coordinate = new Coordinate(
                    result.geometry.location.lat,
                    result.geometry.location.lng
            );
            address.setCoordinate(coordinate);
        } catch (Exception e) {

        }

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

    public void updatePassengerAccount(PassengerAccountDto passengerAccountDto) {

    }

    public PassengerPresenter getPassengerAccount() {
        return null;
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
