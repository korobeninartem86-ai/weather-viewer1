package com.weather.weatherviewer.dto;

import com.weather.weatherviewer.entity.Location;

public class LocationWeatherCard {
    private Location location;
    private WeatherResult weatherResult ;

    public LocationWeatherCard(Location location, WeatherResult weatherResult) {
        this.location = location;
        this.weatherResult = weatherResult;
    }

    public Location getLocation() {
        return location;
    }

    public WeatherResult getWeatherResult() {
        return weatherResult;
    }
}
