package com.example.articleboard.articleboard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Messages {

    SUCCESS(200, "성공"),
    LOGIN_SUCCESS(200, "로그인을 성공했습니다."),
    SIGN_UP_SUCCESS(200, "회원가입이 완료되었습니다."),
    POSTED_SUCCESS(200, "정상적으로 게시되었습니다."),
    BAD_REQUEST(400, "유효한 요청이 아닙니다."),
    FAILED_REQUEST(400, "서버 요청을 실패했습니다."),
    POST_COULD_NOT_BE_POSTED(400, "게시글이 저장되지 않았습니다."),
    POST_COULD_NOT_BE_FOUND(400, "해당 게시물이 없습니다"),
    INVALID_LOGIN(401, "로그인 여부를 확인해주세요."),
    PASSWORD_NOT_MATCHES(401, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_USER_ID(401, "중복된 회원 아이디입니다."),
    EXPIRE_TOKEN(403, "토큰이 만료되었습니다. 다시 로그인 해주세요."),
    NO_SUCH_ELEMENT(404, "존재하지 않습니다."),
    USER_NOT_EXISTED(404, "존재하지 않는 사용자입니다."),
    NO_DATA_NEED_UPDATE(404, "업데이트할 데이터가 없습니다."),
    USER_ALREADY_EXISTS(409, "이미 존재하는 유저입니다");

    private final int statusCode;
    private final String message;
}
