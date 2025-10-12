package br.com.transoft.backend.repository;

import br.com.transoft.backend.constants.ItineraryStatus;
import br.com.transoft.backend.entity.Itinerary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, String> {
    Optional<Itinerary> findItineraryByItineraryIdAndCompany_CompanyId(String itineraryId, String companyId);
    Page<Itinerary> findAllByCompany_CompanyId(String companyId, Pageable pageable);
    int countItineraryByCompany_CompanyId(String companyId);
    int countItineraryByCompany_CompanyIdAndStatus(String companyId, ItineraryStatus status);
}
