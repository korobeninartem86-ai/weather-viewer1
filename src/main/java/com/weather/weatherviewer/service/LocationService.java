package com.weather.weatherviewer.service;

import com.weather.weatherviewer.dao.LocationDao;
import com.weather.weatherviewer.dao.UserDao;
import com.weather.weatherviewer.dao.UserSessionDao;
import com.weather.weatherviewer.entity.Location;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Service
public class LocationService {
    private LocationDao locationDao ;

    public LocationService(LocationDao locationDao) {
        this.locationDao = locationDao;
    }
    public List<Location> findAllByUserId(Long userId){
        return locationDao.findAllByUserId(userId);
    }
    public void addLocation(String name , Long userId, BigDecimal lat ,BigDecimal lon){
        Location location = new Location(name,userId,lat,lon);
        locationDao.save(location);
    }
    public void deleteLocation(int locationId , long userId ){
    locationDao.deleteByIdAndUserId(locationId ,userId);
    }
}
