package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.dto.request.RefreshTokenDto;
import com.example.articleboard.articleboard.domain.dto.response.SignUpResponseDto;
import com.example.articleboard.articleboard.domain.token.RefreshToken;
import com.example.articleboard.articleboard.exception.Messages;
import com.example.articleboard.articleboard.response.DataResponse;
import com.example.articleboard.articleboard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/refreshToken")
    public DataResponse<SignUpResponseDto> refreshToken(@RequestHeader(value = "Refreshtoken") String refreshToken) {
        return new DataResponse<>(Messages.SUCCESS, userService.refreshToken(refreshToken));
    }
}
