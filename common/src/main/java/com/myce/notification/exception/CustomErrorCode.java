package com.myce.notification.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {

    INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "U004", "잘못된 로그인 방식입니다."),
    NOT_EXIST_MESSAGE_TEMPLATE(HttpStatus.NOT_FOUND, "SY001", "메시지 템플릿이 존재하지 않습니다."),
    INVALID_EMAIL_LOG(HttpStatus.NOT_FOUND, "EM001", "유효하지 않은 이메일 로그 입니다."),
    INVALID_EXPO_ADMIN_PERMISSION_TYPE(HttpStatus.NOT_FOUND, "EAD002", "유효하지 않은 권한 타입입니다.");


    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}