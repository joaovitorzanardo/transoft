package br.com.transoft.backend.entity;

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

}
