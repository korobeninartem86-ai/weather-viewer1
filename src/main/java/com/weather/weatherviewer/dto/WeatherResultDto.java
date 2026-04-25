package com.weather.weatherviewer.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
@RequiredArgsConstructor
@Getter
public class WeatherResultDto {
    private final BigDecimal temperature;
    private final BigDecimal feelsLike;
    private final Integer humidity;
    private final String description;
    private final String iconPatch;
}
