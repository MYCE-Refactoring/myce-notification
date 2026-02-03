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
    INVALID_EXPO_ADMIN_PERMISSION_TYPE(HttpStatus.BAD_REQUEST, "EAD002", "유효하지 않은 권한 타입입니다."),

    CORE_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "CO001", "코어 요청이 유효하지 않습니다."),
    CORE_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CO002", "코어 서버 오류입니다."),

    MAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "MO001", "메일 전송에 실패했습니다."),
    MAIL_CONTEXT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MO002", "메일 프롬프트 생성에 실패했습니다."),


    EXPO_EDIT_DENIED(HttpStatus.FORBIDDEN, "EO001", "박람회 수정 권한이 없습니다."),
    EXPO_VIEW_DENIED(HttpStatus.FORBIDDEN, "EO002", "박람회 조회 권한이 없습니다.");




    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}