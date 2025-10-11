package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.Passenger;
import br.com.transoft.backend.entity.route.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, String> {
    Optional<Passenger> findByEmail(String email);
    List<Passenger> findByRoute(Route route);
    Optional<Passenger> findByPassengerIdAndCompany_CompanyId(String passengerId, String companyId);
    Page<Passenger> findAllByCompany_CompanyId(String companyId, Pageable pageable);
    int countAllByCompany_CompanyId(String companyId);
    int countAllByCompany_CompanyIdAndUserAccount_ActiveAndUserAccount_Enabled(String companyId, boolean active, boolean enabled);
    int countAllByCompany_CompanyIdAndUserAccount_Active(String companyId, boolean active);
}
