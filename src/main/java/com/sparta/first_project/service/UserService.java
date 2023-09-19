package com.sparta.first_project.service;

import com.sparta.first_project.dto.ProfileRequestDto;
import com.sparta.first_project.dto.SignupRequestDto;
import com.sparta.first_project.entity.User;
import com.sparta.first_project.entity.UserRoleEnum;
import com.sparta.first_project.jwt.JwtUtil;
import com.sparta.first_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 관리자 인증 토큰
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원가입
    @Transactional
    public void signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email이 존재합니다.");
        }

        //사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 유효하지 않아 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .nickname(requestDto.getNickname())
                .intro(requestDto.getIntro())
                .role(role)
                .build();

        userRepository.save(user);
    }

    // 프로필 수정
    @Transactional
    public void updateProfile(ProfileRequestDto profileRequestDto, User user) {
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.updateprofile(profileRequestDto);
        userRepository.save(user);
    }

    // 구글 로그인 구현
    @Transactional
    public void googleLogin(OAuth2User oAuth2User) {
        // 구글 로그인 성공 시 사용자 정보를 가져옵니다.
        String email = oAuth2User.getAttribute("email");
        String username = oAuth2User.getAttribute("username");

        // 사용자 중복 확인
        Optional<User> checkUser = userRepository.findByEmail(email);
        if (checkUser.isPresent()) {
            // 기존 회원일 경우 로그인 처리합니다.
        } else {
            // 신규 회원일 경우 사용자를 등록합니다.
            User user = User.builder()
                    .email(email)
                    .username(username)
                    .role(UserRoleEnum.USER)
                    .build();
            userRepository.save(user);
        }
    }
}
