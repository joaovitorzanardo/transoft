package br.com.transoft.backend.entity;

import br.com.transoft.backend.constants.PassengerStatusEnum;
import br.com.transoft.backend.dto.itinerary.ItineraryPassengerPresenter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "passenger_status")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PassengerStatus {

    @EmbeddedId
    PassengerStatusKey passengerStatusId;

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
    private PassengerStatusEnum status;

    public ItineraryPassengerPresenter toPresenter() {
        return new ItineraryPassengerPresenter(
                passenger.getPassengerId(),
                passenger.getName(),
                status.getStatus()
        );
    }

}
