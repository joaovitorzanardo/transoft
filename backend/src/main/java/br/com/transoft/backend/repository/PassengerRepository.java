package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.Passenger;
import br.com.transoft.backend.entity.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, String> {
    Optional<Passenger> findByEmail(String email);
    List<Passenger> findByRoute(Route route);
}
