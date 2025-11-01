package br.com.transoft.backend.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
        super("Email ou senha incorretos!");
    }
}
