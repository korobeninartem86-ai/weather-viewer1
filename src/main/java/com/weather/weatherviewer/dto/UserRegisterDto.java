package com.weather.weatherviewer.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
public class UserRegisterDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = " login must be between 3 and 20 characters long")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 4, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 4, message = "Password must be at least 6 characters long")
    private String repeatPassword;

}
