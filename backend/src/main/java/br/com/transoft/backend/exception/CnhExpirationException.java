package br.com.transoft.backend.exception;

public class CnhExpirationException extends RuntimeException {
    public CnhExpirationException() {
        super("The CNH is already expired.");
    }
}
