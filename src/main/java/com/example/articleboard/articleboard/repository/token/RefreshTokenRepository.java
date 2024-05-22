package com.example.articleboard.articleboard.repository.token;

import com.example.articleboard.articleboard.domain.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Transactional
    void deleteByRefreshToken(String refreshToken);
}
