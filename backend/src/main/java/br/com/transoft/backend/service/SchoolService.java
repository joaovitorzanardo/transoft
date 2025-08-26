package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.school.SchoolDto;
import br.com.transoft.backend.dto.school.SchoolPresenter;
import br.com.transoft.backend.entity.Address;
import br.com.transoft.backend.entity.Coordinate;
import br.com.transoft.backend.entity.School;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.SchoolRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final GeoApiContext geoApiContext;

    public SchoolService(SchoolRepository schoolRepository, GeoApiContext geoApiContext) {
        this.schoolRepository = schoolRepository;
        this.geoApiContext = geoApiContext;
    }

    public void saveSchool(SchoolDto schoolDto) {
        // TODO: create mapper for this
        Address address = Address.builder()
                .cep(schoolDto.getAddress().getCep())
                .street(schoolDto.getAddress().getStreet())
                .district(schoolDto.getAddress().getDistrict())
                .number(schoolDto.getAddress().getNumber())
                .complement(schoolDto.getAddress().getComplement())
                .city(schoolDto.getAddress().getCity())
                .uf(schoolDto.getAddress().getUf())
                //.coordinate(new Coordinate("123121", "783457834"))
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

        School school = School.builder()
                .schoolId(UUID.randomUUID().toString())
                .name(schoolDto.getName())
                .address(address)
                .build();

        this.schoolRepository.save(school);
    }

    public List<SchoolPresenter> listSchools(int page, int size) {
        return this.schoolRepository.findAll(PageRequest.of(page, size)).stream().map(School::toPresenter).collect(Collectors.toList());
    }

    public SchoolPresenter findSchoolById(String id) {
        return this.schoolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("School id was not found.")).toPresenter();
    }

}
