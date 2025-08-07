package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.Automaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutomakerRepository extends JpaRepository<Automaker, String> {
    Optional<Automaker> findByNameIgnoreCase(String name);
}
