package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.route.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
    Optional<Route> findByRouteIdAndCompany_CompanyId(String routeId, String companyId);
    Page<Route> findAllByCompany_CompanyId(String companyId, Pageable pageable);
}
