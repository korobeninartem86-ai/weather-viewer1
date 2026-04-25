package com.weather.weatherviewer.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RegisterException extends RuntimeException {
    private final String field;
    private final String codeError;

}
