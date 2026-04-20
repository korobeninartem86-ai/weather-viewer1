package com.weather.weatherviewer.dto;

import java.math.BigDecimal;

public class WeatherResult {
    private BigDecimal temperature;
    private BigDecimal feelsLike ;
    private Integer humidity;
    private String description ;
    private String iconPatch;


    public WeatherResult(BigDecimal temperature, BigDecimal feelsLike, Integer humidity, String description,String iconPatch) {
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.description = description;
        this.iconPatch=iconPatch;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public BigDecimal getFeelsLike() {
        return feelsLike;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public String getDescription() {
        return description;
    }

    public String getIconPatch() {
        return iconPatch;
    }
}
