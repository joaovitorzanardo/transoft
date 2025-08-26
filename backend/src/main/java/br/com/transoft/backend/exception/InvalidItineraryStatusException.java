package br.com.transoft.backend.exception;

public class InvalidItineraryStatusException extends RuntimeException {
    public InvalidItineraryStatusException(String message) {
        super(message);
    }
}
