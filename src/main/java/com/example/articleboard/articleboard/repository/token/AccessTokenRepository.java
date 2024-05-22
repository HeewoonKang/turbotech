package com.example.articleboard.articleboard.repository.token;

import com.example.articleboard.articleboard.domain.token.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
}
