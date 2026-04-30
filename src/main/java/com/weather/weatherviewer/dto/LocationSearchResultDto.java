package com.weather.weatherviewer.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
@RequiredArgsConstructor
@Getter
public class LocationSearchResultDto {
    private final String name;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final String country;


}
