package com.sparta.first_project.dto;

import com.sparta.first_project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDto {
    private String username;
    private String password;
    private String email;
    private String intro;

    public ProfileResponseDto(User user) {
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.intro = user.getIntro();
    }
}
