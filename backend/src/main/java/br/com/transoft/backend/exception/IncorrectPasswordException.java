package br.com.transoft.backend.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
        super("Incorrect email/password!");
    }
}
