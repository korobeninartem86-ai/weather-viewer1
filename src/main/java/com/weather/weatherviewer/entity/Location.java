package com.weather.weatherviewer.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(name = "name",nullable = false)
    private String name ;

@Column(name = "userid",nullable = false)
    private long userId ;

@Column(name = "latitude")
    private BigDecimal latitude ;

@Column(name ="longitude")
    private BigDecimal longitude ;

    public Location(String name, long userId, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(){

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }
}
