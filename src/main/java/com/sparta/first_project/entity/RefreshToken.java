package com.sparta.first_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "username", nullable = false)
    private String username;

    public RefreshToken(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}