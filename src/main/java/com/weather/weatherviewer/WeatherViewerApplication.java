package com.weather.weatherviewer;

import com.weather.weatherviewer.dao.UserDao;
import com.weather.weatherviewer.entity.Users;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(
        exclude = org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
)
public class WeatherViewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherViewerApplication.class, args);
    }

}
