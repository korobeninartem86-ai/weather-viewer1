package com.weather.weatherviewer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginDto {
    @NotBlank(message = "Username cannot be empty")
    private  String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
