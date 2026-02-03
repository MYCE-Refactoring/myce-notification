package com.myce.notification.dto.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MailSendContextRequest {
    Long expoId;
    String entranceStatus;
    String name;
    String phone;
    String reservationCode;
    String ticketName;
}
