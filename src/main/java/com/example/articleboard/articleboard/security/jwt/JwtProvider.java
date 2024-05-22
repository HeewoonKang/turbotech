package com.example.articleboard.articleboard.security.jwt;

import com.example.articleboard.articleboard.domain.RoleType;
import com.example.articleboard.articleboard.security.auth.AuthDetailsImpl;
import com.example.articleboard.articleboard.security.auth.AuthDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final String CLASS_NAME = getClass().getSimpleName();
    @Value("${jwt.access}")
    private long accessToken;

    @Value("${jwt.refresh}")
    private long refreshToken;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final AuthDetailsService auth;

    public String generateAccessToken(Long id) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessToken * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .claim("type", "access_token")
                .setSubject(id.toString())
                .compact();
    }

    public String generateRefreshToken(Long id) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshToken * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .claim("type", "refresh_token")
                .setSubject(id.toString())
                .compact();
    }

    public Date getExpiredAtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public String resolvedToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(header);
        log.info("{} >>> resolvedToken >>> bearerToken: {}", CLASS_NAME, bearerToken);

        if (bearerToken != null) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody().getSubject();
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("type").equals("refresh_token");
    }

    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token) {
        log.info("{} >>> token: {}", CLASS_NAME, token);
        AuthDetailsImpl authDetail = auth.loadUserByUsername(getUserId(token));
        return new UsernamePasswordAuthenticationToken(authDetail, "", authDetail.getAuthorities());
    }
}
