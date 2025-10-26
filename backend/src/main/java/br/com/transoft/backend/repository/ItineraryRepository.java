package br.com.transoft.backend.repository;

import br.com.transoft.backend.constants.ItineraryStatus;
import br.com.transoft.backend.entity.Itinerary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, String> {
    Optional<Itinerary> findItineraryByItineraryIdAndCompany_CompanyId(String itineraryId, String companyId);
    Page<Itinerary> findAllByCompany_CompanyId(String companyId, Pageable pageable);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.passengerStatus st " +
            "JOIN st.passenger p " +
            "where p.passengerId = :passengerId " +
                "AND (i.status = 'AGENDADO' OR i.status = 'EM_ANDAMENTO') " +
                "AND i.date >= CURRENT_DATE " +
            "ORDER BY i.date, i.startTime ASC ")
    Page<Itinerary> findItinerariesByPassengerId(String passengerId, Pageable pageable);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.passengerStatus st " +
            "JOIN st.passenger p " +
            "where p.passengerId = :passengerId " +
                "AND (i.status = 'CANCELADO' OR i.status = 'CONCLUIDO') " +
                "AND i.date <= CURRENT_DATE " +
            "ORDER BY i.date, i.startTime DESC ")
    Page<Itinerary> findItinerariesHistoryByPassengerId(String passengerId, Pageable pageable);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.driver d " +
            "where d.driverId = :driverId " +
            "AND (i.status = 'AGENDADO' OR i.status = 'EM_ANDAMENTO') " +
            "ORDER BY i.date, i.startTime ASC ")
    Page<Itinerary> findItinerariesByDriverId(String driverId, Pageable pageable);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.driver d " +
            "where d.driverId = :driverId " +
            "AND (i.status = 'CANCELADO' OR i.status = 'CONCLUIDO') " +
            "ORDER BY i.date, i.startTime DESC ")
    Page<Itinerary> findItinerariesHistoryByDriverId(String driverId, Pageable pageable);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.driver d " +
            "where d.driverId = :driverId " +
            "ORDER BY i.date, i.startTime ASC LIMIT 1")
    Optional<Itinerary> findNextItineraryForDriver(String driverId);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.passengerStatus st " +
            "JOIN st.passenger p " +
            "where p.passengerId = :passengerId " +
            "AND (i.status = 'AGENDADO') and i.date >= CURRENT_DATE " +
            "ORDER BY i.date, i.startTime ASC LIMIT 1")
    Optional<Itinerary> findNextItineraryForPassenger(String passengerId);

    int countItineraryByCompany_CompanyId(String companyId);
    int countItineraryByCompany_CompanyIdAndStatus(String companyId, ItineraryStatus status);
}
