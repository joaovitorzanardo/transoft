package br.com.transoft.backend.exception;

public class InvalidDateIntervalException extends RuntimeException{
    public InvalidDateIntervalException() {
        super("O intervalo de datas informado é inválido.");
    }
}
