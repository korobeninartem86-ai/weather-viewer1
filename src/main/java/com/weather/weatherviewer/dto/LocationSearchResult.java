package com.weather.weatherviewer.dto;

import java.math.BigDecimal;

public class LocationSearchResult {
    private String name ;
    private BigDecimal lat ;
    private BigDecimal lon ;
    private String country ;

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
