package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {
    Optional<Driver> findByDriverIdAndCompany_CompanyId(String driverId, String companyId);
    Optional<Driver> findByCnhNumberAndCompany_CompanyId(String cnhNumber, String companyId);
    Page<Driver> findAllByCompany_CompanyId(String companyId, Pageable pageable);
    Optional<Driver> findByEmail(String email);
    int countAllByCompany_CompanyId(String companyId);
    int countAllByCompany_CompanyIdAndUserAccount_Active(String companyId, boolean active);
}
