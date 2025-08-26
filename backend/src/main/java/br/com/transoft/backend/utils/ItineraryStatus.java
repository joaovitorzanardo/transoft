package br.com.transoft.backend.utils;

import br.com.transoft.backend.exception.InvalidItineraryStatusException;

public enum ItineraryStatus {
    AGENDADO("AGENDADO"),
    EM_ANDAMENTO("EM_ANDAMENTO"),
    CONCLUIDO("CONCLUIDO");

    private final String status;

    ItineraryStatus(String status) {
        this.status = status;
    }

    public static ItineraryStatus fromString(String status) {
        return switch (status) {
            case "AGENDADO" -> ItineraryStatus.AGENDADO;
            case "EM_ANDAMENTO" -> ItineraryStatus.EM_ANDAMENTO;
            case "CONCLUIDO" -> ItineraryStatus.CONCLUIDO;
            default -> throw new InvalidItineraryStatusException("The status " + status + " is not valid");
        };
    }

    public String getStatus() {
        return this.status;
    }

}
