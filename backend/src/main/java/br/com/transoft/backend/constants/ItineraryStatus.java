package br.com.transoft.backend.constants;

import br.com.transoft.backend.exception.InvalidItineraryStatusException;
import lombok.Getter;

@Getter
public enum ItineraryStatus {

    AGENDADO("AGENDADO"),
    EM_ANDAMENTO("EM_ANDAMENTO"),
    CONCLUIDO("CONCLUIDO"),
    CANCELADO("CANCELADO"),
    PERDIDO("PERDIDO");

    private final String status;

    ItineraryStatus(String status) {
        this.status = status;
    }

    public static ItineraryStatus fromString(String status) {
        for (ItineraryStatus itineraryStatus : ItineraryStatus.values()) {
            if (itineraryStatus.status.equals(status)) {
                return itineraryStatus;
            }
        }

        throw new InvalidItineraryStatusException("The status " + status + " is not valid");
    }

}
