package com.thc.sprbasic2025.repository;

import com.thc.sprbasic2025.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByContent(String content);
    List<RefreshToken> findByUserId(Long userId);
}
