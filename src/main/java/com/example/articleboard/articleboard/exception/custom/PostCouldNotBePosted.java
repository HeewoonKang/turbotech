package com.example.articleboard.articleboard.exception.custom;

import com.example.articleboard.articleboard.exception.BoardException;
import com.example.articleboard.articleboard.exception.Messages;

public class PostCouldNotBePosted extends BoardException {
    public PostCouldNotBePosted() {
        super(Messages.POST_COULD_NOT_BE_POSTED);
    }
}
