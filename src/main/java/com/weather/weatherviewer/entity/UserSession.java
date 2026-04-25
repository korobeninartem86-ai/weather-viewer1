package com.weather.weatherviewer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "sessions")
public class UserSession {

    @Id
    @Column(name = "id")
    private UUID sessionId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "expires")
    private LocalDateTime expiresAt;

    public UserSession(long userId, LocalDateTime expiresAt) {
        this.sessionId = UUID.randomUUID();
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public UserSession() {

    }

}
