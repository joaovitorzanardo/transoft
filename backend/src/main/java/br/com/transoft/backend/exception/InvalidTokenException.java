package br.com.transoft.backend.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("The informed token is not valid.");
    }
}
