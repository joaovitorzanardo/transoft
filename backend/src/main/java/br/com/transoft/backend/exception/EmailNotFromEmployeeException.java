package br.com.transoft.backend.exception;

public class EmailNotFromEmployeeException extends RuntimeException {
    public EmailNotFromEmployeeException(String message) {
        super("The email " + message + " is not from one of the company employee.");
    }
}
