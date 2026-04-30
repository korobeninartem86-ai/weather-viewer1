package com.weather.weatherviewer.controller;

import com.weather.weatherviewer.dto.LocationSearchResultDto;
import com.weather.weatherviewer.service.LocationService;
import com.weather.weatherviewer.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.weather.weatherviewer.controller.AuthController.SESSION_ID;

@RequiredArgsConstructor
@Controller
public class LocationController {
    private final LocationService locationService;
    private final WeatherService weatherService;

    @PostMapping("/locations")
    public String addLocation(@CookieValue(name = SESSION_ID,required = false)UUID sessionId, @RequestParam String name, @RequestParam BigDecimal latitude, @RequestParam BigDecimal longitude) {
        locationService.addLocationBySessionId(name,latitude,longitude,sessionId);
        return "redirect:/home";
            }



    @GetMapping("locations/search")
    public String locationsSearch(@RequestParam String name, Model model) {
        List<LocationSearchResultDto> resultList = weatherService.searchLocations(name);
        model.addAttribute("results", resultList);
        return "search-result";

    }

}
