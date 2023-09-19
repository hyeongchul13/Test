package com.sparta.first_project.controller;

import com.sparta.first_project.dto.BaseResponse;
import com.sparta.first_project.dto.ProfileRequestDto;
import com.sparta.first_project.dto.SignupRequestDto;
import com.sparta.first_project.dto.SuccessResponse;
import com.sparta.first_project.entity.User;
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
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(hidden = true)//swagger에서 보이지 않게 설정
    // 회원 가입
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

    // 회원정보 조회
    @GetMapping("/profile")
    public ResponseEntity<BaseResponse> getProfile(@RequestParam String username) {
        User user = userService.getProfile(username);
        return ResponseEntity.ok().body(new SuccessResponse("회원정보 조회 성공", user));
    }

    // 회원정보 수정
    @PutMapping("/profile")
    public ResponseEntity<BaseResponse> updateProfile(@RequestBody @Valid ProfileRequestDto requestDto,
                                                      BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
        userService.updateProfile(requestDto);
        return ResponseEntity.ok().body(new SuccessResponse("회원정보 수정 성공"));
    }

    // 회원 탈퇴
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id, @RequestParam String password) {
        User user = userService.findById(id);
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        userService.delete(id, password);
        return ResponseEntity.ok().body(new SuccessResponse("회원 탈퇴 성공"));
    }
}