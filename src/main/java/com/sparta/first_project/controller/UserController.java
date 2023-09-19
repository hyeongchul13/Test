package com.sparta.first_project.controller;

import com.sparta.first_project.dto.BaseResponse;
import com.sparta.first_project.dto.SignupRequestDto;
import com.sparta.first_project.dto.SuccessResponse;
import com.sparta.first_project.error.ParameterValidationException;
import com.sparta.first_project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @Operation(hidden = true)//swagger에서 보이지 않게 설정
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
    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/sign-up")
    public String signupPage() {
        return "sign-up";
    }
}

