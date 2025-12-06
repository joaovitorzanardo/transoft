package br.com.transoft.backend.exception;

public class CookieExpiredException extends RuntimeException {
    public CookieExpiredException() {
        super("The cookie has expired.");
    }
}
