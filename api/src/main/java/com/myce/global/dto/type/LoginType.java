package com.myce.global.dto.type;

import com.myce.notification.exception.CustomErrorCode;
import com.myce.notification.exception.CustomException;

public enum LoginType {
    MEMBER, ADMIN_CODE;

    public static LoginType fromString(String type) {
        for (LoginType loginType : LoginType.values()) {
            if (loginType.toString().equals(type)) {
                return loginType;
            }
        }

        throw new CustomException(CustomErrorCode.INVALID_LOGIN_TYPE);
    }
}
