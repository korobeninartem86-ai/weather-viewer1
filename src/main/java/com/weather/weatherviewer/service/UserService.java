package com.weather.weatherviewer.service;

import com.weather.weatherviewer.dao.UserDao;
import com.weather.weatherviewer.dao.UserSessionDao;
import com.weather.weatherviewer.entity.UserSession;
import com.weather.weatherviewer.entity.Users;
import com.weather.weatherviewer.exception.LoginException;
import org.springframework.stereotype.Service;
import com.weather.weatherviewer.exception.RegisterException;

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

    private boolean validateUsernameAndPassword(String username, String password) {
        return username != null && !username.isBlank() && password != null && !password.isBlank();
    }

    public void registerUser(String username, String password) {
        Users user = userDao.findByUsername(username);
        if (user != null) {
            throw new RegisterException("username", "USERNAME_ALREADY_EXISTS", "Account with this username already exists");
        }
        user = new Users(username, password);
        userDao.save(user);
    }

    public UserSession loginUser(String username, String password) {
        Users user = userDao.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new LoginException("INVALID_CREDENTI_ALS", "Incorrect username or password");
        }
        LocalDateTime now = LocalDateTime.now();
        UserSession userSession = new UserSession(user.getId(), now.plusDays(2));
        userSessionDao.save(userSession);
        return userSession;
    }

    public Users findUserBySessionId(UUID sessionId) {
        UserSession userSession = userSessionDao.findBySessionId(sessionId);
        if (userSession == null) {
            throw new RuntimeException("Session not found");
        }
        LocalDateTime now = LocalDateTime.now();
        if (userSession.getExpiresAt().isBefore(now)) {
            throw new RuntimeException("Session expired");
        }
        Users user = userDao.findById(userSession.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    public void logout(UUID sessionId) {
        userSessionDao.deleteBySessionId(sessionId);
    }

    public boolean existsByUsername(String username) {
        Users user = userDao.findByUsername(username);
        return user != null;
    }
}





















