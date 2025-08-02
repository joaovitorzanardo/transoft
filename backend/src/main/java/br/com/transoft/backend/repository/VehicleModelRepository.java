package br.com.transoft.backend.repository;

import br.com.transoft.backend.entity.Automaker;
import br.com.transoft.backend.entity.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, String> {
    List<VehicleModel> findAllByAutomaker(Automaker automaker);
}
