package br.com.transoft.backend.constants;

import br.com.transoft.backend.exception.InvalidPassengerItineraryStatusException;
import lombok.Getter;

@Getter
public enum PassengerStatusEnum {

    CONFIRMADO("CONFIRMADO"),
    NAO_VAI("NAO_VAI");

    private String status;

    PassengerStatusEnum(String status) {
        this.status = status;
    }

    public static PassengerStatusEnum fromString(String status) {
        for (PassengerStatusEnum passengerStatus : PassengerStatusEnum.values()) {
            if (passengerStatus.status.equals(status)) {
                return passengerStatus;
            }
        }

        throw new InvalidPassengerItineraryStatusException("The status " + status + " is not valid");
    }

}
