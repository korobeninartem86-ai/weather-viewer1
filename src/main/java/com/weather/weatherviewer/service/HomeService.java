package com.weather.weatherviewer.service;

import com.weather.weatherviewer.dto.LocationWeatherCard;
import com.weather.weatherviewer.dto.WeatherResult;
import com.weather.weatherviewer.entity.Location;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {
    private final LocationService locationService;
    private final WeatherService weatherService ;

    public HomeService(LocationService locationService, WeatherService weatherService) {
        this.locationService = locationService;
        this.weatherService = weatherService;
    }
    public List<LocationWeatherCard> findAllCardsByUserId(Long userId){
        List<Location> locations = locationService.findAllByUserId(userId);
        if (locations.isEmpty()){
            return List.of();
        }
        List<LocationWeatherCard>cards = new ArrayList<>();
        for (Location location:locations){
            WeatherResult weatherResult = weatherService.getWeather(location.getLatitude(),location.getLongitude());
            if (weatherResult==null){
                continue;
            }
            LocationWeatherCard locationWeatherCard = new LocationWeatherCard(location,weatherResult);
            cards.add(locationWeatherCard);
        }
        return cards;
    }
}
