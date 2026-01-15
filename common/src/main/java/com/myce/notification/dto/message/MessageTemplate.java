package com.myce.notification.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageTemplate {
    private String subject;
    private String content;
}
