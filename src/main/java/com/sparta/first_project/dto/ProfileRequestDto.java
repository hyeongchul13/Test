package com.sparta.first_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfileRequestDto {
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).*$", message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자(@#$%^&+=!)를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9가-힣@#$%^&+=!]).*$", message = "올바른 닉네임 형식이 아닙니다.")
    private String nickname;

    @NotBlank(message = "자기소개를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9가-힣@#$%^&+=!]).*$", message = "자기소개를 작성해주세요.")
    private String intro;

    private boolean admin = false;
    private String adminToken = "";

}
