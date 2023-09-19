package com.sparta.first_project.service;

import com.sparta.first_project.dto.SignupRequestDto;
import com.sparta.first_project.entity.User;
import com.sparta.first_project.entity.UserRoleEnum;
import com.sparta.first_project.jwt.JwtUtil;
import com.sparta.first_project.repository.UserRepository;
import com.sparta.first_project.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.first_project.entity.UserRoleEnum.ADMIN;

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
            role = ADMIN;
        }

        // 사용자 등록
        User user = User.builder().username(username).password(password).email(email).intro(requestDto.getIntro()).role(role).build();
        userRepository.save(user);
    }

    // 회원정보 조회
    public User getProfile(String username) {
        // 유효성 검사
        if (username == null) {
            throw new IllegalArgumentException("사용자 이름을 입력해 주세요.");
        }

        // 회원 정보 조회
        return findByUsername(username);
    }

    // 회원정보 수정
    @Transactional
    public void update(User user) {
        userRepository.save(user);
    }

    // 회원탈퇴
    @Transactional
    public void delete(String password) {
        User user = getCurrentAuthenticatedUser();

        // 입력한 비밀번호를 암호화하여 저장된 비밀번호와 비교
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        userRepository.delete(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자 이름의 회원을 찾을 수 없습니다. 사용자 이름: " + username));
    }

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증된 사용자 정보를 가져올 수 없습니다.");
        }
        return ((UserDetailsImpl) authentication.getPrincipal()).getUser();
    }
}