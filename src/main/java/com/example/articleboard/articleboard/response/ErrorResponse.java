package com.example.articleboard.articleboard.response;

import com.example.articleboard.articleboard.exception.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private int statusCode;
    private String message;

    public ErrorResponse(Messages messages) {
        this.statusCode = messages.getStatusCode();
        this.message = messages.getMessage();
    }

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}

