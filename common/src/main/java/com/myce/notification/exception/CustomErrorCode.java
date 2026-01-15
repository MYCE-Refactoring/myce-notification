package com.myce.notification.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {

    INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "U004", "잘못된 로그인 방식입니다."),
    NOT_EXIST_MESSAGE_TEMPLATE(HttpStatus.NOT_FOUND, "SY001", "메시지 템플릿이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}