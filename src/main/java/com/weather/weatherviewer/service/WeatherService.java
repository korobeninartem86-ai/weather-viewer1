package com.weather.weatherviewer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weatherviewer.dto.LocationSearchResultDto;
import com.weather.weatherviewer.dto.WeatherResultDto;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public WeatherService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public WeatherService(HttpClient client) {
        this.client = client;
        this.objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    public WeatherResultDto getWeather(BigDecimal latitude, BigDecimal longitude) {
        String base = "https://api.open-meteo.com/v1/forecast";
        String url = base + "?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,,apparent_temperature,weather_code,relative_humidity_2m";
        System.out.println("URL=" + url);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("User-Agent", "Mozila/5.0").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Open-meteo request failed");

        }
        String json = response.body();
        System.out.println("BODY" + json);
        JsonNode root = objectMapper.readTree(json);
        JsonNode currentWeather = root.get("current");
        if (currentWeather == null) {
            throw new RuntimeException("current not found");
        }
        BigDecimal temperature = currentWeather.get("temperature_2m").decimalValue();
        BigDecimal feelsLike = currentWeather.get("apparent_temperature").decimalValue();
        int humidity = currentWeather.get("relative_humidity_2m").intValue();
        int code = currentWeather.get("weather_code").intValue();
        String description = mapWeatherCode(code);
        String iconCode = mapWeatherIcon(code);
        return new WeatherResultDto(temperature, feelsLike, humidity, description, iconCode);

    }

    @SneakyThrows
    public List<LocationSearchResultDto> searchLocations(String city) {
        String base = "https://geocoding-api.open-meteo.com/v1/search";
        String name = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String count = "5";
        String language = "en";
        String url = base + "?name=" + name + "&count=" + count + "&language=" + language + "format=json";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("User-Agent", "Mozila/5.0").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<LocationSearchResultDto> locationSearchResultDtos = new ArrayList<>();
        if (response.statusCode() != 200) {
            throw new RuntimeException("Open-meteo request failed");
        }
        String json = response.body();
        JsonNode root = objectMapper.readTree(json);
        JsonNode resultJson = root.get("results");
        if (resultJson == null || !resultJson.isArray() || resultJson.isEmpty()) {
            throw new RuntimeException("Locations not found");
        }
        for (JsonNode item : resultJson) {
            String nameLocation = item.get("name").asText();
            BigDecimal latitude = item.get("latitude").decimalValue();
            BigDecimal longitude = item.get("longitude").decimalValue();
            String country = item.get("country_code").asText();
            LocationSearchResultDto locationSearchResultDto = new LocationSearchResultDto(nameLocation, latitude, longitude, country);
            locationSearchResultDtos.add(locationSearchResultDto);
        }


        return locationSearchResultDtos;

    }

    private String mapWeatherCode(int code) {
        if (code == 0) return "Clear sky";
        if (code == 1) return "Mainly clear";
        if (code == 2) return "Partly cloudy";
        if (code == 3) return "Overcast";
        if (code == 45 || code == 48) return "Fog";
        if (code == 51 || code == 53 || code == 55) return "Drizzle";
        if (code == 61 || code == 63 || code == 65) return "Rain";
        if (code == 71 || code == 73 || code == 75) return "Snow";
        if (code == 95) return "Thunderstorm";
        return "Unknown";
    }

    private String mapWeatherIcon(int code) {
        if (code == 0 || code == 1) return "/image/clear.png";
        if (code == 2 || code == 3) return "/image/cloudy.png";
        if (code == 45 || code == 48) return "/image/fog.png";
        if (code == 51 || code == 53 || code == 55) return "/image/rain.png";
        if (code == 61 || code == 63 || code == 65) return "/image/rain.png";
        if (code == 71 || code == 73 || code == 75) return "/image/snow.png";
        if (code == 95) return "/image/storm.png";
        return "/image/clear.png";
    }
}
