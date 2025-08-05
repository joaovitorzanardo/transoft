package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {
    Optional<Driver> findByCnhNumber(String cnhNumber);
    Optional<Driver> findByEmail(String email);
}
