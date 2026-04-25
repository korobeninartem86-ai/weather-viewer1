package com.weather.weatherviewer.service;

import com.weather.weatherviewer.dto.LocationWeatherCardDto;
import com.weather.weatherviewer.dto.WeatherResultDto;
import com.weather.weatherviewer.entity.Location;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {
    private final LocationService locationService;
    private final WeatherService weatherService;

    public HomeService(LocationService locationService, WeatherService weatherService) {
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

    public List<LocationWeatherCardDto> findAllCardsByUserId(Long userId) {
        List<Location> locations = locationService.findAllByUserId(userId);
        if (locations.isEmpty()) {
            return List.of();
        }
        List<LocationWeatherCardDto> cards = new ArrayList<>();
        for (Location location : locations) {
            WeatherResultDto weatherResultDto = weatherService.getWeather(location.getLatitude(), location.getLongitude());
            if (weatherResultDto == null) {
                continue;
            }
            LocationWeatherCardDto locationWeatherCardDto = new LocationWeatherCardDto(location, weatherResultDto);
            cards.add(locationWeatherCardDto);
        }
        return cards;
    }
}
