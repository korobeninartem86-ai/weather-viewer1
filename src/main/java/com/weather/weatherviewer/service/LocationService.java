package com.weather.weatherviewer.service;

import com.weather.weatherviewer.dao.LocationDao;
import com.weather.weatherviewer.dao.UserDao;
import com.weather.weatherviewer.dao.UserSessionDao;
import com.weather.weatherviewer.entity.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class LocationService {
    private final LocationDao locationDao;

    public List<Location> findAllByUserId(Long userId) {
        return locationDao.findAllByUserId(userId);
    }

    public void addLocation(String name, Long userId, BigDecimal latitude, BigDecimal longitude) {
        Location location = new Location(name, userId, latitude, longitude);
        locationDao.save(location);
    }

    public void deleteLocation(int locationId, long userId) {
        locationDao.deleteByIdAndUserId(locationId, userId);
    }
}
