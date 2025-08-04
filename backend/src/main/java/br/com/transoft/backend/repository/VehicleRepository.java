package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    Optional<Vehicle> findByPlateNumber(String plateNumber);
}
