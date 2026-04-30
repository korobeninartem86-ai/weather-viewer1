package com.weather.weatherviewer.dto;

import com.weather.weatherviewer.entity.Location;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LocationWeatherCardDto {
    private final Location location;
    private final WeatherResultDto weatherResultDto;
}
