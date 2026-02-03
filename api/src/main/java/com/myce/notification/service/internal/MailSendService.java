package com.myce.notification.service.internal;

import com.myce.notification.dto.email.EmailSendRequest;
import com.myce.notification.dto.email.MultiEmailSendRequest;

public interface MailSendService {
    void sendMail(EmailSendRequest req);
    void sendMailToMultiple(MultiEmailSendRequest req);
    void sendSupportMail(EmailSendRequest req);
}