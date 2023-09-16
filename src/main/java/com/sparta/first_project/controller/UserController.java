package com.sparta.first_project.controller;

import com.sparta.first_project.dto.*;
import com.sparta.first_project.error.ParameterValidationException;
import com.sparta.first_project.security.UserDetailsImpl;
import com.sparta.first_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse> signup(@RequestBody @Valid SignupRequestDto requestDto,
                                               BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
        userService.signup(requestDto);
        return ResponseEntity.ok().body(new SuccessResponse("회원 가입 완료"));
    }
    // 프로필 수정
    @PutMapping("/profile")
    public ResponseEntity<BaseResponse> updateProfile(Long id, @RequestBody @Valid ProfileRequestDto profileRequestDto, BindingResult bindingResult,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);
        ProfileResponseDto profileResponseDto = userService.updateProfile(id, profileRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new SuccessResponse("프로필 수정 완료", profileResponseDto));

    }
    private static void checkParamValidation(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
    }
}

