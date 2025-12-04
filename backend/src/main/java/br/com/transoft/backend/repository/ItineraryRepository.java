package br.com.transoft.backend.repository;

import br.com.transoft.backend.constants.ItineraryStatus;
import br.com.transoft.backend.entity.Itinerary;
import br.com.transoft.backend.entity.route.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, String> {
    Optional<Itinerary> findItineraryByItineraryIdAndCompany_CompanyId(String itineraryId, String companyId);
    Page<Itinerary> findAllByCompany_CompanyId(String companyId, Pageable pageable);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.passengerStatus st " +
            "JOIN st.passenger p " +
            "where c.companyId = :companyId " +
            "AND p.passengerId = :passengerId " +
            "AND (i.status = 'AGENDADO' OR i.status = 'EM_ANDAMENTO') " +
            "AND i.date >= CURRENT_DATE " +
            "ORDER BY i.date ASC, i.startTime ASC ")
    Page<Itinerary> findItinerariesByPassengerId(String companyId, String passengerId, Pageable pageable);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.passengerStatus st " +
            "JOIN st.passenger p " +
            "where c.companyId = :companyId " +
            "AND p.passengerId = :passengerId " +
            "AND i.status = 'CONCLUIDO' " +
            "AND i.date <= CURRENT_DATE " +
            "ORDER BY i.date DESC, i.startTime DESC ")
    Page<Itinerary> findItinerariesHistoryByPassengerId(String companyId, String passengerId, Pageable pageable);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.driver d " +
            "where c.companyId = :companyId " +
            "AND d.driverId = :driverId " +
            "AND i.status IN ('AGENDADO', 'EM_ANDAMENTO') " +
            "AND i.date >= CURRENT_DATE " +
            "ORDER BY i.date ASC, i.startTime ASC ")
    Page<Itinerary> findItinerariesByDriverId(String companyId, String driverId, Pageable pageable);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.driver d " +
            "where c.companyId = :companyId " +
            "AND d.driverId = :driverId " +
            "AND i.status = 'CONCLUIDO' " +
            "ORDER BY i.date DESC, i.startTime DESC ")
    Page<Itinerary> findItinerariesHistoryByDriverId(String companyId, String driverId, Pageable pageable);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.driver d " +
            "where c.companyId = :companyId " +
            "AND d.driverId = :driverId " +
            "AND (i.status = 'AGENDADO') " +
            "and i.date >= CURRENT_DATE " +
            "ORDER BY i.date ASC, i.startTime ASC LIMIT 1")
    Optional<Itinerary> findNextItineraryForDriver(String companyId, String driverId);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.passengerStatus st " +
            "JOIN st.passenger p " +
            "where c.companyId = :companyId " +
            "AND p.passengerId = :passengerId " +
            "AND (i.status = 'AGENDADO') and i.date >= CURRENT_DATE " +
            "ORDER BY i.date ASC, i.startTime ASC LIMIT 1")
    Optional<Itinerary> findNextItineraryForPassenger(String companyId, String passengerId);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.passengerStatus st " +
            "JOIN st.passenger p " +
            "where c.companyId = :companyId " +
            "AND p.passengerId = :passengerId " +
            "AND (i.status = 'EM_ANDAMENTO') " +
            "ORDER BY i.date ASC, i.startTime ASC LIMIT 1")
    Optional<Itinerary> findOngoingItineraryForPassenger(String companyId, String passengerId);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.driver d " +
            "where c.companyId = :companyId " +
            "AND d.driverId = :driverId " +
            "AND (i.status = 'EM_ANDAMENTO') " +
            "ORDER BY i.date ASC, i.startTime ASC LIMIT 1")
    Optional<Itinerary> findOngoingItineraryForDriver(String companyId, String driverId);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.route r " +
            "where c.companyId = :companyId " +
            "AND r.routeId = :routeId " +
            "AND i.status = 'AGENDADO'")
    List<Itinerary> findAllScheduledItinerariesByRouteId(String companyId, String routeId);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.passengerStatus st " +
            "JOIN st.passenger p " +
            "where c.companyId = :companyId " +
            "AND p.passengerId = :passengerId " +
            "AND i.status = 'AGENDADO' ")
    List<Itinerary> findAllScheduledItinerariesByPassengerId(String companyId, String passengerId);

    @Query("SELECT i from Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.route r " +
            "where c.companyId = :companyId " +
            "AND r.routeId = :routeId " +
            "AND i.status = 'AGENDADO' ")
    List<Itinerary> findAllItinerariesByScheduledItinerariesByRouteId(String companyId, String routeId);

    @Query("SELECT i FROM Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.vehicle v " +
            "WHERE c.companyId = :companyId " +
            "AND v.vehicleId = :vehicleId " +
            "AND i.status IN ('AGENDADO', 'EM_ANDAMENTO')")
    List<Itinerary> findAllScheduledAndOngoingItinerariesByVehicleId(String companyId, String vehicleId);

    @Query("SELECT i FROM Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.driver d " +
            "WHERE c.companyId = :companyId " +
            "AND d.driverId = :driverId " +
            "AND i.status IN ('AGENDADO', 'EM_ANDAMENTO')")
    List<Itinerary> findAllScheduledAndOngoingItinerariesByDriverId(String companyId, String driverId);

    @Query("SELECT i FROM Itinerary i " +
            "JOIN i.company c " +
            "JOIN i.route r " +
            "WHERE r.routeId = :routeId " +
            "AND c.companyId = :companyId " +
            "AND i.date BETWEEN :startDate AND :endDate")
    List<Itinerary> findItinerariesInDatesByRouteId(String companyId,
                                                    String routeId,
                                                    LocalDate startDate,
                                                    LocalDate endDate
    );

    @Query("SELECT i FROM Itinerary i " +
            "WHERE i.status = 'AGENDADO' " +
            "AND i.date < CURRENT_DATE")
    List<Itinerary> findMissedItineraries();

    int countItineraryByCompany_CompanyId(String companyId);
    int countItineraryByCompany_CompanyIdAndStatus(String companyId, ItineraryStatus status);
}