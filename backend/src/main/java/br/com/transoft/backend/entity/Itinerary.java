package br.com.transoft.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "itinerary")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Itinerary {

    @Id
    @Column(name = "itinerary_id")
    private String itineraryId;




}
