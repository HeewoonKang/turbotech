package com.example.articleboard.articleboard.exception;

import lombok.Getter;

/**
 * 커스텀 에러 클래스
 */
@Getter
public class BoardException extends RuntimeException {

    private final Messages errorCode;

    public BoardException(Messages errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BoardException(String message, Messages errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
