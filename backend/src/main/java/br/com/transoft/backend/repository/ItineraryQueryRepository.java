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

    public List<Itinerary> findByFilter(ItineraryFilter filter, String companyId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Itinerary> query = criteriaBuilder.createQuery(Itinerary.class);
        Root<Itinerary> root = query.from(Itinerary.class);

        List<Predicate> predicates = new ArrayList<>();

        Join<Itinerary, Company> company = root.join("company");

        predicates.add(criteriaBuilder.equal(company.get("companyId"), companyId));

        if (Objects.nonNull(filter.status()) && !filter.status().isEmpty()) {
            List<String> status = filter.status().stream().map(String::toUpperCase).toList();
            predicates.add(root.get("status").in(status));
        }

        if (Objects.nonNull(filter.type()) && !filter.type().isEmpty()) {
            predicates.add(root.get("type").in(filter.type()));
        }

        if (Objects.nonNull(filter.date())) {
            predicates.add(criteriaBuilder.equal(root.get("date"), filter.date()));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

}
