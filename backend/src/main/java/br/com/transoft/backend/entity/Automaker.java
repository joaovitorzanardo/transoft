package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.vehicle.presenter.AutomakerPresenter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "automaker")
public class Automaker {

    @Id
    @Column(name = "automaker_id")
    private String automakerId;

    @Column(name = "name")
    private String name;

    public Automaker() {
    }

    public Automaker(String automakerId, String name) {
        this.automakerId = automakerId;
        this.name = name;
    }

    public Automaker(String name) {
        this.automakerId = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getAutomakerId() {
        return automakerId;
    }

    public String getName() {
        return name;
    }

    public AutomakerPresenter toDto() {
        return new AutomakerPresenter(automakerId, name);
    }

}
