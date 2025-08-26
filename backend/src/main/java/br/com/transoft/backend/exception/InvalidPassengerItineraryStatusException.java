package br.com.transoft.backend.exception;

public class InvalidPassengerItineraryStatusException extends RuntimeException {
  public InvalidPassengerItineraryStatusException(String message) {
    super(message);
  }
}
