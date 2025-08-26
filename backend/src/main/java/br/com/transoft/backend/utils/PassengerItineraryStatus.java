package br.com.transoft.backend.utils;

import br.com.transoft.backend.exception.InvalidPassengerItineraryStatusException;

public enum PassengerItineraryStatus {
    CONFIRMADO("CONFIRMADO"),
    NAO_VAI("NAO_VAI");

    private String status;

    PassengerItineraryStatus(String status) {
        this.status = status;
    }

    public static PassengerItineraryStatus fromString(String status) {
        return switch (status) {
            case "CONFIRMADO" -> PassengerItineraryStatus.CONFIRMADO;
            case "NAO_VAI" -> PassengerItineraryStatus.NAO_VAI;
            default -> throw new InvalidPassengerItineraryStatusException("The status " + status + " is not valid");
        };
    }

    public String getStatus() {
        return this.status;
    }

}
