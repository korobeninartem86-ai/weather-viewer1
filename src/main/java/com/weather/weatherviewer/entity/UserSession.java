package com.weather.weatherviewer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
public class UserSession {

    @Id
    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public UserSession(long userId, LocalDateTime expiresAt) {
        this.sessionId = UUID.randomUUID();
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public UserSession() {

    }

    public UUID getSessionId() {
        return sessionId;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
