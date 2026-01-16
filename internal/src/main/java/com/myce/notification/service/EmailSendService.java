package com.myce.notification.service;

import com.myce.notification.dto.request.EmailSendRequest;
import com.myce.notification.dto.request.MultiEmailSendRequest;

import java.util.List;

public interface EmailSendService {
    void sendMail(EmailSendRequest req);
    void sendMailToMultiple(MultiEmailSendRequest req);
    void sendSupportMail(EmailSendRequest req);
}