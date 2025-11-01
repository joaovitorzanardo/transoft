package br.com.transoft.backend.exception;

public class DisabledUserException extends RuntimeException {
    public DisabledUserException() {
        super("Usuário desabilitado!");
    }
}
