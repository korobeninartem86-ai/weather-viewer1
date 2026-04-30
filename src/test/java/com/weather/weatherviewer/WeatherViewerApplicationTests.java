package com.weather.weatherviewer;

import com.weather.weatherviewer.dao.UserDao;
import com.weather.weatherviewer.dao.UserSessionDao;
import com.weather.weatherviewer.entity.UserSession;
import com.weather.weatherviewer.entity.Users;
import com.weather.weatherviewer.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class WeatherViewerApplicationTests {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserSessionDao userSessionDao;

    @Test
    void registerUser_createsNewUser(){
        String username = "Misha4";
        String password = "1234";

        userService.registerUser(username , password);
        Users user = userDao.findByUsername(username);

        assertNotNull(user);
        assertEquals(username,user.getUsername());


    }

    @Test
    void register_duplicate_throwsException(){
        String username = "Misha5";
        String password = "1234";

        userService.registerUser(username , password);
        RuntimeException ex = assertThrows(RuntimeException.class,()->{userService.registerUser(username , password);
        });
        assertEquals("User: "+username+" already exists",ex.getMessage());
    }

    @Test
    void login_session_verification(){
        String username = "Misha6";
        String password = "1234";
        userService.registerUser(username , password);
        Users user = userDao.findByUsername(username);
        assertNotNull(user);
        UserSession userSession= userService.loginUser(user.getUsername(),user.getPassword());
        UserSession userSessionBD = userSessionDao.findBySessionId(userSession.getSessionId());
        assertEquals(userSession.getSessionId() , userSessionBD.getSessionId());
        assertEquals(user.getId(),userSessionBD.getUserId());
    }
    @Test
    void logout_delete_session(){
        String username = "Misha9";
        String password = "1234";
        userService.registerUser(username , password);
        Users user = userDao.findByUsername(username);
        UserSession userSession= userService.loginUser(user.getUsername(),user.getPassword());
        assertNotNull(userSession);
        userService.logout(userSession.getSessionId());
        UserSession userSession1 = userSessionDao.findBySessionId(userSession.getSessionId());
        assertNull(userSession1);
    }
    @Test
    void incorrect_password(){
        String username = "Misha19";
        String password = "1234";
        userService.registerUser(username , password);
        Users user = userDao.findByUsername(username);
        List<UserSession>sessionsBefore = userSessionDao.findAllByUserId(user.getId());
        assertEquals(0, sessionsBefore.size());
        assertThrows(RuntimeException.class,()->{userService.loginUser(user.getUsername(),"321321");
        });
        List<UserSession>sessionsAfter = userSessionDao.findAllByUserId(user.getId());
        assertEquals(0,sessionsAfter.size());
    }

    @Test
    void invalid_session(){
        String username = "Misha22";
        String password = "1234";
        userService.registerUser(username , password);
        Users user = userDao.findByUsername(username);
        UserSession userSession = new UserSession(user.getId(), LocalDateTime.now().minusDays(2));
        userSessionDao.save(userSession);
        RuntimeException ex = assertThrows(RuntimeException.class,()->{userService.findUserBySessionId(userSession.getSessionId());
        });
        assertEquals("Session expired", ex.getMessage());

    }
    @Test
    void valid_session(){
        String username = "Misha24";
        String password = "1234";
        userService.registerUser(username , password);
        Users user = userDao.findByUsername(username);
        UserSession userSession = new UserSession(user.getId(),LocalDateTime.now().plusDays(1));
        userSessionDao.save(userSession);
        Users user2 = userService.findUserBySessionId(userSession.getSessionId());
        assertNotNull(user2);
        assertEquals(user.getId(),user2.getId());
    }
    @Test
    void getWeather_returnsParsedWeatherResult(){

    }
}
