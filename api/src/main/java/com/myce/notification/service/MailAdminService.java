package com.myce.notification.service;

import com.myce.global.dto.type.LoginType;
import com.myce.notification.dto.email.ExpoAdminEmailRequest;

public interface MailAdminService {
    void sendMail(Long memberId,
                  LoginType loginType,
                  Long expoId,
                  ExpoAdminEmailRequest dto,
                  String entranceStatus,
                  String name,
                  String phone,
                  String reservationCode,
                  String ticketName);
}
