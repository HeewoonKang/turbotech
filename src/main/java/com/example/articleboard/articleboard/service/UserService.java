package com.example.articleboard.articleboard.service;

import com.example.articleboard.articleboard.domain.RoleType;
import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.domain.dto.request.UserLoginRequestDto;
import com.example.articleboard.articleboard.domain.dto.request.UserSignUpRequestDto;
import com.example.articleboard.articleboard.domain.dto.response.SignUpResponseDto;
import com.example.articleboard.articleboard.exception.custom.PasswordIsNotMatchesException;
import com.example.articleboard.articleboard.exception.custom.UserAlreadyExists;
import com.example.articleboard.articleboard.exception.custom.UserNotFoundException;
import com.example.articleboard.articleboard.security.jwt.JwtProvider;
import com.example.articleboard.articleboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 유저 정보 서비스 클래스
 */

@Slf4j
@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Transactional(readOnly = true)
    public long countUser() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Page<User> getUserList(Pageable pageable)
    {
        return userRepository.findAll(pageable);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean UsernameAvailable(String username) {
        return !userRepository.findByUsername(username).isPresent();
    }

    /**
     * 회원 가입
     *
     * @param signUpDto
     * @return
     */
    public void signUp(UserSignUpRequestDto signUpDto) {

        /*if (userRepository.findByUsername(signUpDto.getUsername()).isPresent()) throw new UserAlreadyExists();
        userRepository.save(new User(signUpDto.getUsername(), passwordEncoder.encode(signUpDto.getPassword()),
                signUpDto.getName(), signUpDto.getNickName()));*/
        if (userRepository.findByUsername(signUpDto.getUsername()).isPresent()) {
            throw new UserAlreadyExists();
        }

        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword())); // 비밀번호는 암호화하여 저장
        user.setFamilyName(signUpDto.getFamilyName());
        user.setName(signUpDto.getName());
        user.setNickName(signUpDto.getNickName());
        user.setBirth(signUpDto.getBirth());
        user.setContact(signUpDto.getContact());
        user.setEtcContact(signUpDto.getEtcContact());
        user.setRegion(signUpDto.getRegion());
        user.setRole(RoleType.valueOf(signUpDto.getRole()));
        user.setEmail(signUpDto.getEmail());
        user.setPostcode(signUpDto.getPostcode());
        user.setCounty(signUpDto.getCounty());
        user.setCity(signUpDto.getCity());
        user.setAddress(signUpDto.getAddress());
        user.setEtcAddress(signUpDto.getEtcAddress());
        user.setSerialNumber(signUpDto.getSerialNumber());

        userRepository.save(user);
    }
    /**
     * 로그인
     *
     * @param loginDto
     * @return
     */
    public SignUpResponseDto login(UserLoginRequestDto loginDto)
    {
        User findUser = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(UserNotFoundException::new);
        // dto 비밀번호와 서버 내 암호화된 비밀번호가 같은지 확인
        if (passwordEncoder.matches(loginDto.getPassword(), findUser.getPassword())) {

            String accessToken = jwtProvider.generateAccessToken(findUser.getId());
            String refreshToken = jwtProvider.generateRefreshToken(findUser.getId());
            Date expiredAt = jwtProvider.getExpiredAtToken(accessToken);

            log.info("{} >>> userName: {}", CLASS_NAME, findUser.getUsername());
            log.info("{} >>> accessToken : {}", CLASS_NAME, accessToken);
            log.info("{} >>> refreshToken : {}", CLASS_NAME, refreshToken);

            return SignUpResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .nickName(findUser.getNickName())
                    .role(findUser.getRole())
                    .expiredAt(expiredAt.getTime())
                    .build();
        } else throw new PasswordIsNotMatchesException();
    }

    public SignUpResponseDto refreshToken(String refreshToken) {
        if (jwtProvider.validateToken(refreshToken)) {
            Long userId = Long.valueOf(jwtProvider.getUserId(refreshToken));
            User findUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

            String accessToken = jwtProvider.generateAccessToken(findUser.getId());
            Date expiredAt = jwtProvider.getExpiredAtToken(accessToken);

            return SignUpResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .nickName(findUser.getNickName())
                    .role(findUser.getRole())
                    .expiredAt(expiredAt.getTime())
                    .build();
        } else {
            throw new UserNotFoundException();
        }
    }

    public User find(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
