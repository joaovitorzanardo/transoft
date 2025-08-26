package br.com.transoft.backend.entity;

import br.com.transoft.backend.utils.PassengerItineraryStatus;
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

    @Id
    @Column(name = "passenger_itinerary_id")
    private String passengerItineraryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PassengerItineraryStatus passengerItineraryStatus;

}
