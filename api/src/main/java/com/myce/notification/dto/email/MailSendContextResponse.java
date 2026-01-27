package com.myce.notification.dto.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class MailSendContextResponse {
    String expoName;
    String contactPhone;
    String contactEmail;
    List<RecipientInfoDto> recipientInfos;
}
