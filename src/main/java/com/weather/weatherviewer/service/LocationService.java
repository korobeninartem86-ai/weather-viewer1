package com.weather.weatherviewer.service;

import com.weather.weatherviewer.dao.LocationDao;
import com.weather.weatherviewer.dao.UserDao;
import com.weather.weatherviewer.dao.UserSessionDao;
import com.weather.weatherviewer.entity.Location;
import com.weather.weatherviewer.entity.UserSession;
import com.weather.weatherviewer.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class LocationService {
    private final LocationDao locationDao;
    private  final UserService userService;

    public List<Location> findAllByUserId(Long userId) {
        return locationDao.findAllByUserId(userId);
    }

    public void addLocationBySessionId(String name,BigDecimal latitude, BigDecimal longitude , UUID sessionId) {
        if (name == null || latitude==null || longitude==null ){
            return;
        }
        Users user = userService.getAuthorizedUser(sessionId);
        Location location = new Location(name, user.getId(), latitude, longitude);
        locationDao.save(location);
    }

    public void deleteLocation(int locationId, UUID sessionId) {
        Users user = userService.getAuthorizedUser(sessionId);
        locationDao.deleteByIdAndUserId(locationId, user.getId());
    }
}
