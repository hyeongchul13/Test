package com.sparta.first_project.controller;

import com.sparta.first_project.dto.BaseResponse;
import com.sparta.first_project.dto.ProfileRequestDto;
import com.sparta.first_project.dto.SignupRequestDto;
import com.sparta.first_project.dto.SuccessResponse;
import com.sparta.first_project.error.ParameterValidationException;
import com.sparta.first_project.security.UserDetailsImpl;
import com.sparta.first_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
//    private final GoogleService googleService;

    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/sign-up")
    public String signupPage() {
        return "sign-up";
    }

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
    public ResponseEntity<BaseResponse> updateProfile(@RequestBody @Valid ProfileRequestDto profileRequestDto, BindingResult bindingResult,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);
        userService.updateProfile(profileRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new SuccessResponse("프로필 수정 완료"));
    }
    @GetMapping("/oauth2/google/login")
    public String googleLogin() {
        return "redirect:/oauth2/authorize/google";
    }

    @GetMapping("/oauth2/google/callback")
    public String googleCallback(OAuth2User oAuth2User, Model model) {
        model.addAttribute("user", oAuth2User);
        return "home";
    }

    private static void checkParamValidation(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
    }
}

