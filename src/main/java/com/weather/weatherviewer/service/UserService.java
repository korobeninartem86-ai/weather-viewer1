package com.weather.weatherviewer.service;

import com.weather.weatherviewer.dao.UserDao;
import com.weather.weatherviewer.dao.UserSessionDao;
import com.weather.weatherviewer.dto.UserLoginDto;
import com.weather.weatherviewer.dto.UserRegisterDto;
import com.weather.weatherviewer.entity.UserSession;
import com.weather.weatherviewer.entity.Users;
import com.weather.weatherviewer.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserDao userDao;
    private final UserSessionDao userSessionDao;

    public boolean registerUser(UserRegisterDto registerDto , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return false;
        }
        if (!registerDto.getPassword().equals(registerDto.getRepeatPassword())){
            bindingResult.rejectValue("repeatPassword","PASSWORDS_DONT_MATCH","Password do not match");
            return false;
        }
        Users user = userDao.findByUsername(registerDto.getUsername());
        if (user != null) {
            bindingResult.rejectValue("username", "USERNAME_ALREADY_EXISTS", "Account with this username already exists");
            return false;
        }
        user = new Users(registerDto.getUsername(), registerDto.getPassword());
        userDao.save(user);
        return true;
    }

    public Optional<UUID> loginUser(UserLoginDto userLoginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Optional.empty();
        }
        Users user = userDao.findByUsername(userLoginDto.getUsername());
        if (user == null || !user.getPassword().equals(userLoginDto.getPassword())) {
            bindingResult.rejectValue("username","INVALID_CREDENTI_ALS", "Incorrect username or password");
            return Optional.empty();
        }
        LocalDateTime now = LocalDateTime.now();
        UserSession userSession = new UserSession(user.getId(), now.plusDays(2));
        userSessionDao.save(userSession);
        return Optional.of(userSession.getSessionId());
    }

    public Users getAuthorizedUser(UUID sessionId) {
        UserSession userSession = userSessionDao.findBySessionId(sessionId);
        if (sessionId==null){
            throw new UnauthorizedException("session is not valid");
        }
        if (userSession == null) {
            throw new UnauthorizedException("session is not valid");
        }
        LocalDateTime now = LocalDateTime.now();

        if (userSession.getExpiresAt().isBefore(now)) {
            throw new UnauthorizedException("session is not valid");
        }
        Users user = userDao.findById(userSession.getUserId());
        if (user == null) {
            throw new UnauthorizedException("session is not valid");
}
        return user;
    }

    public void logout(UUID sessionId) {
        if (sessionId==null){
            return;
        }
        userSessionDao.deleteBySessionId(sessionId);
    }


}





















