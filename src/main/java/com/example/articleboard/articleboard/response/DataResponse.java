package com.example.articleboard.articleboard.response;


import com.example.articleboard.articleboard.exception.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공통 응답 패턴
 *
 * @param <T>
 */
@Getter
@NoArgsConstructor
public class DataResponse<T> {

    private int statusCode;
    private String message;
    private T result;

    public DataResponse(Messages messages) {
        this.statusCode = messages.getStatusCode();
        this.message = messages.getMessage();
        this.result = null;
    }

    public DataResponse(Messages messages, T data) {
        this.statusCode = messages.getStatusCode();
        this.message = messages.getMessage();
        this.result = data;
    }
}

