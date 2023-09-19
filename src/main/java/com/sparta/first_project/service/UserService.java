package com.sparta.first_project.service;

import com.sparta.first_project.dto.ProfileRequestDto;
import com.sparta.first_project.dto.SignupRequestDto;
import com.sparta.first_project.entity.Post;
import com.sparta.first_project.entity.User;
import com.sparta.first_project.entity.UserRoleEnum;
import com.sparta.first_project.jwt.JwtUtil;
import com.sparta.first_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.first_project.entity.UserRoleEnum.ADMIN;
import static com.sparta.first_project.entity.UserRoleEnum.USER;

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

//    public void updateProfile(ProfileRequestDto requestDto) {
//        String username = requestDto.getUsername();
//        User user = findByUsername(username);
//
//        // 입력한 비밀번호를 BCryptPasswordEncoder를 사용하여 검사
//        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//        // 회원 정보 수정
//        user.setUsername(requestDto.getUsername());
//        user.setPassword(requestDto.getPassword());
//        user.setEmail(requestDto.getEmail());
//        user.setIntro(requestDto.getIntro());
//
//
//        // 회원 정보 저장
//        userRepository.save(user);
//    }

    // 회원 정보 삭제
    public void delete(Long id, String password) {
        User user = findById(id);
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.delete(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자 이름의 회원을 찾을 수 없습니다. 사용자 이름: " + username));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다. ID: " + id));
    }

    @Transactional
    public void update(User user) {
        userRepository.save(user);
    }
}