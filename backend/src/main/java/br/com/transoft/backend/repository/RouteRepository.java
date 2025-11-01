package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.route.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
    Optional<Route> findByRouteIdAndCompany_CompanyId(String routeId, String companyId);
    Page<Route> findAllByCompany_CompanyId(String companyId, Pageable pageable);
    List<Route> findAllByCompany_CompanyId(String companyId);
    List<Route> findAllByCompany_CompanyIdAndActiveTrue(String companyId);
    List<Route> findAllRoutesByCompany_CompanyIdAndDefaultVehicle_VehicleId(String companyId, String vehicleId);
    List<Route> findAllRoutesByCompany_CompanyIdAndDefaultDriver_DriverId(String companyId, String diverId);
    int countAllByCompany_CompanyId(String companyId);
    int countAllByCompany_CompanyIdAndActive(String companyId, Boolean active);
}
