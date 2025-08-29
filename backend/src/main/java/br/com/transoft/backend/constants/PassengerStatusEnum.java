package br.com.transoft.backend.constants;

import br.com.transoft.backend.exception.InvalidPassengerItineraryStatusException;

public enum PassengerStatus {
    CONFIRMADO("CONFIRMADO"),
    NAO_VAI("NAO_VAI");

    private String status;

    PassengerStatus(String status) {
        this.status = status;
    }

    public static PassengerStatus fromString(String status) {
        for (PassengerStatus passengerStatus : PassengerStatus.values()) {
            if (passengerStatus.status.equals(status)) {
                return passengerStatus;
            }
        }

        throw new InvalidPassengerItineraryStatusException("The status " + status + " is not valid");
    }

    public String getStatus() {
        return this.status;
    }

}
