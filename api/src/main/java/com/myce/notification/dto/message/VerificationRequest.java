package com.myce.notification.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {
    String email;
    String verificationName;
    String code;
    String limitTime;
}
