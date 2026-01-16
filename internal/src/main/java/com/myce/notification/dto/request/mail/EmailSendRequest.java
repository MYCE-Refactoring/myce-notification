package com.myce.notification.dto.request.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendRequest {

    String to;
    String subject;
    String content;
}
