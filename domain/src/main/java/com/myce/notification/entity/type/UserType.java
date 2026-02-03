package com.myce.notification.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    MEMBER("회원"),
    GUEST("비회원");

    private final String label;
}
