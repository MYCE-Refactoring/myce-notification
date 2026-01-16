package com.myce.notification.dto.email;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MailSendContextResponse {
    String expoName;
    String contactPhone;
    String contactEmail;
    List<RecipientInfoDto> recipientInfos;
}
