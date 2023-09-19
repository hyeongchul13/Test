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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
//    private final GoogleService googleService;

    @GetMapping("/login/page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    @Operation(hidden = true)//swagger에서 보이지 않게 설정
    @PostMapping("/signup")
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


    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<BaseResponse> withdraw(@RequestParam("id") Long id, @RequestParam("password") String password) {
        userService.withdraw(id, password);
        return ResponseEntity.ok()
                .body(new SuccessResponse("회원 탈퇴 성공"));
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

