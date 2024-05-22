package com.example.articleboard.articleboard.exception.custom;

import com.example.articleboard.articleboard.exception.BoardException;
import com.example.articleboard.articleboard.exception.Messages;

public class PasswordIsNotMatchesException extends BoardException {
    public PasswordIsNotMatchesException() {
        super(Messages.PASSWORD_NOT_MATCHES);
    }
}
