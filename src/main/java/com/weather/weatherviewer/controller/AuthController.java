package com.weather.weatherviewer.controller;


import com.weather.weatherviewer.dto.HomePageDto;
import com.weather.weatherviewer.dto.UserLoginDto;
import com.weather.weatherviewer.dto.UserRegisterDto;
import com.weather.weatherviewer.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Controller
public class AuthController {
    private final UserService userService;
    private final LocationService locationService;
    private final HomeService homeService;
    public static final String SESSION_ID = "sessionId";

    @GetMapping("/register")
    public String registerUserPage(Model model) {
        model.addAttribute("user",new UserRegisterDto());
        return "register";
    }


    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegisterDto dto, BindingResult bindingResult) {
 if (userService.registerUser(dto,bindingResult)){
     return "redirect:/login";
 }
        return "register";
    }


    @PostMapping("/locations/delete")
    public String locationDelete(@RequestParam("locationId") int id, @CookieValue(name = SESSION_ID,required = false)UUID sessionId) {
        if (sessionId==null){
            return "redirect:/login";
        }
        locationService.deleteLocation(id,sessionId);
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user",new UserRegisterDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") UserLoginDto userLoginDto, BindingResult bindingResult, HttpServletResponse response) {
        Optional<UUID> userSessionId = userService.loginUser(userLoginDto,bindingResult);
        if (userSessionId.isEmpty()){
            return "login";
        }
        Cookie cookie = new Cookie(SESSION_ID, userSessionId.get().toString());
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(@CookieValue(name = SESSION_ID,required = false)UUID sessionId,Model model) {
        HomePageDto homePageDto = homeService.getHomePage(sessionId);
        model.addAttribute("user", homePageDto.getUser());
        model.addAttribute("cards", homePageDto.getCards());
        return "home";
    }


    @PostMapping("/logout")
    public String logout(@CookieValue(name = SESSION_ID,required = false)UUID sessionId, HttpServletResponse response) {
        userService.logout(sessionId);
        Cookie deleteCoolie = new Cookie(SESSION_ID, null);
        deleteCoolie.setMaxAge(0);
        deleteCoolie.setPath("/");
        response.addCookie(deleteCoolie);
        return "redirect:/login";
            }
        }



