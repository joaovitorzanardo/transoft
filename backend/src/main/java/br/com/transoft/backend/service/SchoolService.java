package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.school.SchoolDto;
import br.com.transoft.backend.dto.school.SchoolPresenter;
import br.com.transoft.backend.entity.Address;
import br.com.transoft.backend.entity.School;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.mapper.AddressMapper;
import br.com.transoft.backend.repository.SchoolRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final CoordinateService coordinateService;

    public SchoolService(SchoolRepository schoolRepository, CoordinateService coordinateService) {
        this.schoolRepository = schoolRepository;
        this.coordinateService = coordinateService;
    }

    public void saveSchool(SchoolDto schoolDto) {
        Address address = AddressMapper.toEntity(
                schoolDto.getAddress(),
                coordinateService.findCoordinateByAddress(schoolDto.getAddress().toString())
        );

        School school = School.builder()
                .schoolId(UUID.randomUUID().toString())
                .name(schoolDto.getName())
                .address(address)
                .build();

        schoolRepository.save(school);
    }

    public List<SchoolPresenter> listSchools(int page, int size) {
        return schoolRepository.findAll(PageRequest.of(page, size)).stream().map(School::toPresenter).collect(Collectors.toList());
    }

    public School findSchoolById(String schoolId) {
        return schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException("School id was not found."));
    }

}
