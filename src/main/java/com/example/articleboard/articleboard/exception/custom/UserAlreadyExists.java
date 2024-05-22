package com.example.articleboard.articleboard.exception.custom;

import com.example.articleboard.articleboard.exception.BoardException;
import com.example.articleboard.articleboard.exception.Messages;

public class UserAlreadyExists extends BoardException {
    public UserAlreadyExists() {
        super(Messages.USER_ALREADY_EXISTS);
    }
}
