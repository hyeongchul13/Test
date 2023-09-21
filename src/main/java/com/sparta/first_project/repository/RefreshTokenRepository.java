package com.sparta.first_project.repository;

import com.sparta.first_project.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByUsername(String username);

    boolean existsByToken(String refresh);
}
