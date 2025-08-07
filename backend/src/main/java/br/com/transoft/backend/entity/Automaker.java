package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.vehicle.presenter.AutomakerPresenter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "automaker")
@Getter
@Setter
@NoArgsConstructor
public class Automaker {

    @Id
    @Column(name = "automaker_id")
    private String automakerId;

    @Column(name = "name", nullable = false)
    private String name;

    public Automaker(String name) {
        this.automakerId = UUID.randomUUID().toString();
        this.name = name.trim();
    }

    public AutomakerPresenter toPresenter() {
        return new AutomakerPresenter(automakerId, name);
    }

}
