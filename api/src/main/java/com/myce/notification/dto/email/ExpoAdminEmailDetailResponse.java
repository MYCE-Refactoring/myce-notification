package com.myce.notification.dto.email;

import com.myce.notification.document.EmailLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ExpoAdminEmailDetailResponse {
    private String id;
    private String subject;
    private String content;
    private Integer recipientCount;
    private List<EmailLog.RecipientInfo> recipientInfos;
    private LocalDateTime createdAt;
}
