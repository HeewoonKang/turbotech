package com.example.articleboard.articleboard.domain.dto.response;

import com.example.articleboard.articleboard.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 / 로그인 성공 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponseDto {
    private String nickName;
    private String accessToken;
    private String refreshToken;
    private Long expiredAt;
    private RoleType role;
}
