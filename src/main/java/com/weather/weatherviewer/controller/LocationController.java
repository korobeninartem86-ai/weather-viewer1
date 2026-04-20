package com.weather.weatherviewer.controller;

import com.weather.weatherviewer.dto.LocationSearchResult;
import com.weather.weatherviewer.entity.Users;
import com.weather.weatherviewer.service.LocationService;
import com.weather.weatherviewer.service.UserService;
import com.weather.weatherviewer.service.WeatherService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
public class LocationController {
    private final LocationService locationService;
    private final UserService userService;
    private final WeatherService weatherService;

    public LocationController(LocationService locationService, UserService userService, WeatherService weatherService) {
        this.locationService = locationService;
        this.userService = userService;
        this.weatherService = weatherService;
    }

    @PostMapping("/locations")
    public String addLocation(@RequestParam String name , @RequestParam BigDecimal lat ,@RequestParam BigDecimal lon, HttpServletRequest request){
        Cookie[]cookies = request.getCookies();
        if (cookies==null){
            return "redirect:/login";
        }
        for (Cookie cookie : cookies){
            if (cookie.getName().equals("sessionId")){
                UUID sessionId = UUID.fromString(cookie.getValue());
                Users user = userService.findUserBySessionId(sessionId);
                locationService.addLocation(name ,user.getId(),lat,lon);
                break;
            }
        }
        return "redirect:/home";
    }
@GetMapping("locations/search")
    public String locationsSearch(@RequestParam String name , Model model){
        List<LocationSearchResult> resultList = weatherService.searchLocations(name);
    model.addAttribute("results",resultList);
    return "search-result";

}

}
