package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.Itinerary;
import br.com.transoft.backend.entity.Passenger;
import br.com.transoft.backend.entity.PassengerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerStatusRepository extends JpaRepository<PassengerStatus, String> {
    Optional<PassengerStatus> findByItineraryAndPassenger(Itinerary itinerary, Passenger passenger);
    List<PassengerStatus> findByItinerary(Itinerary itinerary);
}
