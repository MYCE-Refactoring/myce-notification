package com.myce.notification.dto.email;

import com.myce.global.dto.type.LoginType;
import com.myce.notification.common.ExpoAdminPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EnsureExpoPermissionRequest {
    Long expoId;
    Long memberId;
    LoginType loginType;
    ExpoAdminPermission permission;
}
