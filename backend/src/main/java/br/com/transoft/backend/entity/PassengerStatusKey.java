package br.com.transoft.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
@Getter
@Setter
public class PassengerItineraryKey implements Serializable {

    @Column(name = "passenger_id")
    private String passengerId;

    @Column(name = "itinerary_id")
    private String itineraryId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PassengerItineraryKey that = (PassengerItineraryKey) o;
        return Objects.equals(passengerId, that.passengerId) && Objects.equals(itineraryId, that.itineraryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passengerId, itineraryId);
    }
}
