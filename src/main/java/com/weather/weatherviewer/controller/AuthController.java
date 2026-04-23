package com.weather.weatherviewer.controller;


import com.sun.net.httpserver.HttpServer;
import com.weather.weatherviewer.dto.LocationWeatherCard;
import com.weather.weatherviewer.dto.UserLoginDto;
import com.weather.weatherviewer.dto.UserRegisterDto;
import com.weather.weatherviewer.dto.WeatherResult;
import com.weather.weatherviewer.entity.Location;
import com.weather.weatherviewer.entity.UserSession;
import com.weather.weatherviewer.entity.Users;
import com.weather.weatherviewer.exception.LoginException;
import com.weather.weatherviewer.exception.RegisterException;
import com.weather.weatherviewer.service.HomeService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private final HomeService homeService;

    public AuthController(UserService userService, LocationService locationService, WeatherService weatherService,HomeService homeService) {
        this.userService = userService;
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.homeService = homeService;
    }
    @GetMapping("/register")
    public String registerUserPage(){
    return "register";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegisterDto dto , BindingResult bindingResult, Model model){
    if (!bindingResult.getAllErrors().isEmpty()) {
        return "register";
    }
        try {
            userService.registerUser(dto.getUsername(),dto.getPassword());
        } catch (RegisterException e) {
            bindingResult.rejectValue(e.getField(),e.getCodeError(),e.getMessage());
            return "register";
        }
        return "redirect:/login";
    }


    @PostMapping("/locations/delete")
    public String locationDelete (@RequestParam("locationId")int id,HttpServletRequest request){
                Users user = getCurrentUser(request);
                if (user==null){
                    return "redirect:/login";
                }
                locationService.deleteLocation(id,user.getId());
                return "redirect:/home";
    }

    @GetMapping("/login")
    public String showLoginPage(){
    return "login";
}
@PostMapping("/login")
    public String login(@ModelAttribute("user")UserLoginDto userLoginDto,BindingResult bindingResult, HttpServletResponse response ) {
    if (bindingResult.hasErrors()){
        return "login" ;
    }
    UUID sessionId;
    try {
        sessionId = userService.loginUser(userLoginDto.getUsername(), userLoginDto.getPassword()).getSessionId();
    } catch (LoginException e) {
        bindingResult.reject(e.getCodeError(),e.getMessage());
        return "login";
    }
    Cookie cookie = new Cookie("sessionId", sessionId.toString());
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
                List<LocationWeatherCard>cards = homeService.findAllCardsByUserId(user.getId());
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

