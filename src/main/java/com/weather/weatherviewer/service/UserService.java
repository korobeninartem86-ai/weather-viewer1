package com.weather.weatherviewer.service;

import com.weather.weatherviewer.dao.UserDao;
import com.weather.weatherviewer.dao.UserSessionDao;
import com.weather.weatherviewer.entity.UserSession;
import com.weather.weatherviewer.entity.Users;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private final UserDao userDao;
    private final UserSessionDao userSessionDao;

    public UserService(UserDao userDao, UserSessionDao userSessionDao) {
        this.userDao = userDao;
        this.userSessionDao = userSessionDao;
    }
    private boolean validateUsernameAndPassword(String username , String password){
        if (username==null || username.isBlank() || password==null || password.isBlank()){
            return false;
        }
        return true;
    }

    public Users registerUser(String username , String password){
        if (!validateUsernameAndPassword(username,password)){
            throw new RuntimeException(" Username or password is empty");
        }
        Users user = userDao.findByUsername(username);
        if (user != null){
            throw new RuntimeException("User: "+username+" already exists");
        }
        user = new Users(username,password);
        userDao.save(user);
        return user;
    }
    public UserSession loginUser (String username , String password){
        if (!validateUsernameAndPassword(username,password)){
            throw new RuntimeException(" Username or password is empty");
        }
        Users user = userDao.findByUsername(username);
        if (user==null){
            throw new RuntimeException("User: "+username+" not exists");
        }
        if (!user.getPassword().equals(password)){
            throw new RuntimeException("incorrect password");
        }         LocalDateTime now = LocalDateTime.now();
        UserSession userSession = new UserSession(user.getId(),now.plusDays(2));
        userSessionDao.save(userSession);
        return userSession;
    }
    public Users findUserBySessionId(UUID sessionId){
        UserSession userSession = userSessionDao.findBySessionId(sessionId);
        if (userSession == null){
            throw new RuntimeException("Session not found");
        }
        LocalDateTime now = LocalDateTime.now();
        if (userSession.getExpiresAt().isBefore(now)){
            throw new RuntimeException("Session expired");
        }
        Users user = userDao.findById(userSession.getUserId());
        if (user == null){
            throw new RuntimeException("User not found");
        }
        return user;
    }
    public  void logout (UUID sessionId){
        userSessionDao.deleteBySessionId(sessionId);
    }

    public boolean existsByUsername(String username){
    Users user = userDao.findByUsername(username);
    if (user != null){
        return true;
    }
    return false;
    }
}





















