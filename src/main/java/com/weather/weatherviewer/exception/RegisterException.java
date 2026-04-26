package com.weather.weatherviewer.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class RegisterException extends RuntimeException {
    private final String field;
    private final String codeError;


    public RegisterException(String field, String codeError ,String message) {
        super(message);
        this.field = field;
        this.codeError = codeError;
    }
}
