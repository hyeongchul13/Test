package com.sparta.first_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    @Pattern(message = "유저이름 똑바로 하세요",regexp = "^[a-z0-9]{4,10}$")
    private String username;
    @NotBlank
    @Pattern(message = "비밀번호 똑바로 하세요",regexp = "^[a-z0-9A-Z@$!%*?&]{8,15}$")
    private String password;
    private boolean admin = false;
    private String adminToken ="";
}
