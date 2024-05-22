package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.domain.dto.request.UserLoginRequestDto;
import com.example.articleboard.articleboard.domain.dto.request.UserSignUpRequestDto;
import com.example.articleboard.articleboard.domain.dto.response.ResponseDto;
import com.example.articleboard.articleboard.domain.dto.response.SignUpResponseDto;
import com.example.articleboard.articleboard.exception.Messages;
import com.example.articleboard.articleboard.response.DataResponse;
import com.example.articleboard.articleboard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 유저 관련 컨트롤러
 */

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    // 회원가입
    /*@PostMapping(value = "/signUp")
    public void signUp(@RequestBody UserSignUpRequestDto signUpDto)
    {
        userService.signUp(signUpDto);
    }
*/
    @PostMapping("/checkUsername")
    public ResponseEntity<?> checkUsername(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        boolean isAvailable = userService.UsernameAvailable(username);
        return ResponseEntity.ok(Collections.singletonMap("isAvailable", isAvailable));
    }
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpRequestDto signUpDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMap);
        }

        userService.signUp(signUpDto);
        return ResponseEntity.ok().build();
    }
    // 로그인
    @PostMapping(value = "/login")
    public DataResponse<SignUpResponseDto> login(@RequestBody UserLoginRequestDto user)
    {
        return new DataResponse<>(Messages.LOGIN_SUCCESS, userService.login(user));
    }
}
