package com.myce.notification.dto.email;

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
