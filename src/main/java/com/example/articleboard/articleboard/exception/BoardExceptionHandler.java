package com.example.articleboard.articleboard.exception;

import com.example.articleboard.articleboard.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 에러 핸들러
 * BaseException을 타고 들어와 핸들러를 거쳐 response로 보내줌.
 */
@ControllerAdvice
@Slf4j
public class BoardExceptionHandler {
    @ExceptionHandler(BoardException.class)
    protected ResponseEntity<ErrorResponse> handlerException(final BoardException e) {
        log.error("handlerException 작동 >>> {}", e.getMessage());

        final Messages errorCode = e.getErrorCode();
        return new ResponseEntity<>(new ErrorResponse(errorCode),
                HttpStatus.valueOf(errorCode.getStatusCode()));
    }
}
