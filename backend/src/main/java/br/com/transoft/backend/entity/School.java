package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.school.SchoolPresenter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "school")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class School {

    @Id
    @Column(name = "school_id")
    private String schoolId;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Address address;

    public SchoolPresenter toPresenter() {
        return new SchoolPresenter(schoolId, name, address.toDto());
    }

}
