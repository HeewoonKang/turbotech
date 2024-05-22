package com.example.articleboard.articleboard.exception.custom;

import com.example.articleboard.articleboard.exception.BoardException;
import com.example.articleboard.articleboard.exception.Messages;

public class UserNotFoundException extends BoardException {
    public UserNotFoundException() {
        super(Messages.USER_NOT_EXISTED);
    }
}
