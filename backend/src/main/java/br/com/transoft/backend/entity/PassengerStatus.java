package br.com.transoft.backend.entity;

import br.com.transoft.backend.constants.PassengerItineraryStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "passenger_itinerary")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PassengerItinerary {

    @EmbeddedId
    PassengerStatusKey passengerItineraryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("passengerId")
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itineraryId")
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PassengerItineraryStatus passengerItineraryStatus;

}
