package com.weather.weatherviewer.exception;

import lombok.Getter;

@Getter
public class LoginException extends RuntimeException{
    private final String codeError;

    public LoginException(String codeError, String message) {
        super(message);
        this.codeError = codeError;
    }

}
