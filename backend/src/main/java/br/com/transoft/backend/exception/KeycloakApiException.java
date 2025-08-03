package br.com.transoft.backend.exception;

public class KeycloakApiException extends RuntimeException {
    public KeycloakApiException(String message) {
        super(message);
    }
}
