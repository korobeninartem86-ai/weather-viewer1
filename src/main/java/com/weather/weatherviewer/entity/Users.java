package com.weather.weatherviewer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Getter
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Setter
    @Getter
    @Column(nullable = false)
    private String password;

    public Users() {

    }

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
