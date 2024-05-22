package com.example.articleboard.articleboard.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {
    private Long id;
    private String userName;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
}
