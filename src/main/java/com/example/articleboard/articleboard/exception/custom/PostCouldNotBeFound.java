package com.example.articleboard.articleboard.exception.custom;

import com.example.articleboard.articleboard.exception.BoardException;
import com.example.articleboard.articleboard.exception.Messages;

public class PostCouldNotBeFound extends BoardException {
    public PostCouldNotBeFound() {
        super(Messages.POST_COULD_NOT_BE_FOUND);
    }
}
