package com.weather.weatherviewer.dto;

import com.weather.weatherviewer.entity.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Getter
@RequiredArgsConstructor
public class HomePageDto {
    private final Users user;
    private final List<LocationWeatherCardDto> cards;
}
