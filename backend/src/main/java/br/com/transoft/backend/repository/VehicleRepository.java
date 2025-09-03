package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    Optional<Vehicle> findByPlateNumberAndCompany_CompanyId(String plateNumber, String companyId);
    Optional<Vehicle> findByVehicleIdAndCompany_CompanyId(String vehicleId, String companyId);
    Page<Vehicle> findAllByCompany_CompanyId(String companyId, Pageable pageable);
}
