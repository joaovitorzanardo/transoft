package br.com.transoft.backend.repository;

import br.com.transoft.backend.dto.itinerary.ItineraryFilter;
import br.com.transoft.backend.entity.Company;
import br.com.transoft.backend.entity.Itinerary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ItineraryQueryRepository {

    private final EntityManager entityManager;

    public ItineraryQueryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Itinerary> findByFilter(ItineraryFilter filter, String companyId, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Itinerary> query = criteriaBuilder.createQuery(Itinerary.class);
        Root<Itinerary> root = query.from(Itinerary.class);

        List<Predicate> predicates = buildPredicates(criteriaBuilder, root, filter, companyId);
        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Long countByFilter(ItineraryFilter filter, String companyId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Itinerary> root = query.from(Itinerary.class);

        List<Predicate> predicates = buildPredicates(criteriaBuilder, root, filter, companyId);

        query.select(criteriaBuilder.count(root));
        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getSingleResult();
    }

    private List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<Itinerary> root,
                                            ItineraryFilter filter, String companyId) {
        List<Predicate> predicates = new ArrayList<>();

        Join<Itinerary, Company> company = root.join("company");
        Join<Itinerary, Company> route = root.join("route");
        Join<Itinerary, Company> driver = root.join("driver");
        Join<Itinerary, Company> vehicle = root.join("vehicle");

        predicates.add(criteriaBuilder.equal(company.get("companyId"), companyId));

        if (Objects.nonNull(filter.status()) && !filter.status().isEmpty()) {
            List<String> status = filter.status().stream().map(String::toUpperCase).toList();
            predicates.add(root.get("status").in(status));
        }

        if (Objects.nonNull(filter.type()) && !filter.type().isEmpty()) {
            List<String> types = filter.type().stream().map(String::toUpperCase).toList();
            predicates.add(root.get("type").in(types));
        }

        if (Objects.nonNull(filter.date())) {
            predicates.add(criteriaBuilder.equal(root.get("date"), filter.date()));
        }

        if (Objects.nonNull(filter.driverId()) && !filter.driverId().isEmpty()) {
            predicates.add(criteriaBuilder.equal(driver.get("driverId"), filter.driverId()));
        }

        if (Objects.nonNull(filter.routeId()) && !filter.routeId().isEmpty()) {
            predicates.add(criteriaBuilder.equal(route.get("routeId"), filter.routeId()));
        }

        if (Objects.nonNull(filter.vehicleId()) && !filter.vehicleId().isEmpty()) {
            predicates.add(criteriaBuilder.equal(vehicle.get("vehicleId"), filter.vehicleId()));
        }

        return predicates;
    }

}
