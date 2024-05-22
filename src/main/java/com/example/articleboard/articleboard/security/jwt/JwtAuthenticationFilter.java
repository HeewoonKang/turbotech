package com.example.articleboard.articleboard.security.jwt;


import com.example.articleboard.articleboard.exception.Messages;
import com.example.articleboard.articleboard.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.google.gson.Gson;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    private final Environment environment;

    Gson gson = new Gson(); // JSON 형태의 데이터를 JAVA객체로 변환(직렬화, 역직렬화 용)
    ObjectMapper objectMapper = new ObjectMapper();

    /*private final List<String> openApiEndpoints = Arrays.asList(
            "/user/login",
            "/user/signUp"
    );*/

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        log.info("필터");
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        log.info("RequestURI: {}", servletRequest.getRequestURI());
        log.info("Method: {}", servletRequest.getMethod());

        String token = jwtProvider.resolvedToken(servletRequest);
        System.out.println("token: " + token);
        if (!((HttpServletRequest) request).getRequestURI().contains("error")) {
            if (token != null && jwtProvider.validateToken(token)) {
                log.info("Token 뽑힘: {}", token );
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                returnError((HttpServletResponse) response, 403, "만료된 토큰입니다.");
                return;
            }

            log.info("In JwtFilter, token validated");
        }

        chain.doFilter(request, response);
    }

    private void returnError(HttpServletResponse response, Integer status, String message) {
        response.setStatus(Messages.EXPIRE_TOKEN.getStatusCode());
        log.info("returnError >>> entered. status : {}, message: {}", status, message);
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(status, message);
        try {
//          String json = objectMapper.writeValueAsString(errorResponse);
            String json = gson.toJson(errorResponse);   // ErrorResponse 객체를 JSON 문자열로 변환
            log.error("In JwtFilter :" + json);
            response.getWriter().write(json);
        } catch (Exception er) {
            log.error(er.getMessage());
        }
    }
}
