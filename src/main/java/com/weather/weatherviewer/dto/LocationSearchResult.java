package com.weather.weatherviewer.dto;

import java.math.BigDecimal;

public class LocationSearchResult {
    private final String name;
    private final BigDecimal lat;
    private final BigDecimal lon;
    private final String country;

    public LocationSearchResult(String name, BigDecimal lat, BigDecimal lon, String country) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public String getCountry() {
        return country;
    }
}
