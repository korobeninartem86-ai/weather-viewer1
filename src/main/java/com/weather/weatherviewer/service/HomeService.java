package com.weather.weatherviewer.service;

import com.weather.weatherviewer.dto.HomePageDto;
import com.weather.weatherviewer.dto.LocationWeatherCardDto;
import com.weather.weatherviewer.dto.WeatherResultDto;
import com.weather.weatherviewer.entity.Location;
import com.weather.weatherviewer.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final LocationService locationService;
    private final WeatherService weatherService;
    private final UserService userService;

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

    public HomePageDto getHomePage(UUID sessionId){
        Users user = userService.getAuthorizedUser(sessionId);
        List<LocationWeatherCardDto>locationWeatherCardDtoList = findAllCardsByUserId(user.getId());
        return new HomePageDto(user,locationWeatherCardDtoList);
    }
}
