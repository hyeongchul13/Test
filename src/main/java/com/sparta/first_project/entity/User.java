package com.sparta.first_project.entity;

import com.sparta.first_project.dto.ProfileRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String intro;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private Long googleId;

    private boolean deleted;

    public void updateprofile(ProfileRequestDto profileRequestDto) {
        this.password = profileRequestDto.getPassword();
        this.email = profileRequestDto.getEmail();
        this.nickname = profileRequestDto.getNickname();
        this.intro = profileRequestDto.getIntro();
    }

    public User(String username, String password, String email, UserRoleEnum role, Long googleId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.googleId =googleId;
    }

    public User googleIdUpdate(Long googleId) {
        this.googleId = googleId;
        return this;
    }

    public void setDeleted(boolean b) {
        this.deleted = deleted;
    }
}