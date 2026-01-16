package com.myce.notification.service;

import com.myce.notification.dto.request.mail.EmailSendRequest;
import com.myce.notification.dto.request.mail.MultiEmailSendRequest;

public interface MailSendService {
    void sendMail(EmailSendRequest req);
    void sendMailToMultiple(MultiEmailSendRequest req);
    void sendSupportMail(EmailSendRequest req);
}