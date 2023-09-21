package com.sparta.first_project.security;

import com.sparta.first_project.entity.UserRoleEnum;
import com.sparta.first_project.jwt.JwtUtil;
import com.sparta.first_project.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.getJwtFromHeader(req,JwtUtil.AUTHORIZATION_HEADER);
        String refreshToken = jwtUtil.getJwtFromHeader(req,JwtUtil.REFRESH_HEADER);

        if (StringUtils.hasText(accessToken)) {

            if (!jwtUtil.validateToken(accessToken)) {
                String refresh = req.getHeader(JwtUtil.REFRESH_HEADER);
                if (!jwtUtil.validateToken(refreshToken) || !refreshTokenRepository.existsByToken(refresh)){
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    log.info("Token이 만료 되었습니다.");
                    throw new JwtException("Refresh Token Error");
                }

                log.info("Access Token reCreate");
                Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
                String username = info.getSubject();
                UserRoleEnum role = UserRoleEnum.valueOf(String.valueOf(info.get("auth")));

                accessToken = jwtUtil.createToken(username,role);
                res.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
                accessToken = jwtUtil.substringToken(accessToken);
            }

            Claims info = jwtUtil.getUserInfoFromToken(accessToken);

            log.info("Token Authorization");
            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


}