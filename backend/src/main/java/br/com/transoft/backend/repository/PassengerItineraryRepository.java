package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.Itinerary;
import br.com.transoft.backend.entity.Passenger;
import br.com.transoft.backend.entity.PassengerItinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerItineraryRepository extends JpaRepository<PassengerItinerary, String> {
    Optional<PassengerItinerary> findByItineraryAndPassenger(Itinerary itinerary, Passenger passenger);
    List<PassengerItinerary> findByItinerary(Itinerary itinerary);
}
