package com.weather.weatherviewer.controller;


import com.sun.net.httpserver.HttpServer;
import com.weather.weatherviewer.dto.LocationWeatherCard;
import com.weather.weatherviewer.dto.WeatherResult;
import com.weather.weatherviewer.entity.Location;
import com.weather.weatherviewer.entity.UserSession;
import com.weather.weatherviewer.entity.Users;
import com.weather.weatherviewer.service.LocationService;
import com.weather.weatherviewer.service.UserService;
import com.weather.weatherviewer.service.WeatherService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthController {
    private final UserService userService ;
    private final LocationService locationService;
    private final WeatherService weatherService;

    public AuthController(UserService userService, LocationService locationService, WeatherService weatherService) {
        this.userService = userService;
        this.locationService = locationService;
        this.weatherService = weatherService;
    }
    @GetMapping("/register")
    public String registerUserPage(){
    return "register";
    }
    @PostMapping("/register")
    public String registerUser(@RequestParam String username ,@RequestParam String password ,@RequestParam String repeatPassword ,Model model){
        if (!password.equals(repeatPassword)){
            model.addAttribute("globalError","Passwords don't match");
            model.addAttribute("username",username);
            return "register";
        }
        if (userService.existsByUsername(username)){
            model.addAttribute("usernameError","Account with this username already exists");
            model.addAttribute("username",username);
            return "register";

        }
        if (password.length()<4){
            model.addAttribute("passwordError","Password must be at least 4 characters long");
            model.addAttribute("username",username);
            return "register";
        }
    userService.registerUser(username,password);
    return "redirect:/login";
    }
    @PostMapping("/locations/delete")
    public String locationDelete (@RequestParam("locationId")int id,HttpServletRequest request){
                Users user = getCurrentUser(request);
                if (user==null){
                    return "redirect:/login";
                }
                long userId = user.getId();
                locationService.deleteLocation(id,userId);
                return "redirect:/home";
    }

    @GetMapping("/login")
    public String showLoginPage(){
    return "login";
}
@PostMapping("/login")
    public String login(@RequestParam String username , @RequestParam String password , HttpServletResponse response ){
    UUID sessionId = userService.loginUser(username,password).getSessionId();
    Cookie cookie = new Cookie("sessionId",sessionId.toString());
    cookie.setPath("/");
    response.addCookie(cookie);
    return "redirect:/home";
    }
    @GetMapping("/home")
    public String homePage(HttpServletRequest request, Model model){
            Users user = getCurrentUser(request);
                if (user==null){
                    return "redirect:/login";
                }
                List<Location> locations = locationService.findAllByUserId(user.getId());
                List<LocationWeatherCard>cards = new ArrayList<>();
                for (Location location:locations){
                    WeatherResult weatherResult = weatherService.getWeather(location.getLatitude(),location.getLongitude());
                    LocationWeatherCard locationWeatherCard = new LocationWeatherCard(location,weatherResult);
                    cards.add(locationWeatherCard);
                }
                model.addAttribute("locations",locations);
                model.addAttribute("user",user);
                model.addAttribute("cards",cards);
                return "home";

            }


    @PostMapping("/logout")
    public String logout (HttpServletRequest request , HttpServletResponse response){
        Cookie[]cookies = request.getCookies();
        if (cookies == null){
            return "redirect:/login";
        }
        for (Cookie cookie : cookies){
            if ("sessionId".equals(cookie.getName())){
                UUID sessionId = UUID.fromString(cookie.getValue());
                userService.logout(sessionId);
                Cookie deleteCoolie = new Cookie("sessionId",null);
                deleteCoolie.setMaxAge(0);
                deleteCoolie.setPath("/");
                response.addCookie(deleteCoolie);
                return "redirect:/login";
            }
        }
        return "redirect:/login";
    }
    private Users getCurrentUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("sessionId".equals(cookie.getName())) {
                UUID sessionId = UUID.fromString(cookie.getValue());
                return userService.findUserBySessionId(sessionId);
            }
        }
        return null;
    }
    }

