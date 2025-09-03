package br.com.transoft.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class PassengerStatusKey implements Serializable {

    @Column(name = "itinerary_id")
    private String passengerId;

    @Column(name = "passenger_id")
    private String itineraryId;

}
