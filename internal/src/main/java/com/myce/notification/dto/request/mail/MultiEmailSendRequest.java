package com.myce.notification.dto.request.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MultiEmailSendRequest {

    List<String> recipients;
    String subject;
    String content;
}
