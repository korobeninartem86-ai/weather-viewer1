package com.weather.weatherviewer.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class WeatherResult {
    private final BigDecimal temperature;
    private final BigDecimal feelsLike;
    private final Integer humidity;
    private final String description;
    private final String iconPatch;


    public WeatherResult(BigDecimal temperature, BigDecimal feelsLike, Integer humidity, String description, String iconPatch) {
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.description = description;
        this.iconPatch = iconPatch;
    }

}
