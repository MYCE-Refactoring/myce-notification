package com.myce.notification.controller;

import com.myce.notification.dto.request.mail.EmailSendRequest;
import com.myce.notification.dto.request.mail.MultiEmailSendRequest;
import com.myce.notification.dto.request.message.ReservationConfirmRequest;
import com.myce.notification.dto.request.message.ResetRequest;
import com.myce.notification.dto.request.message.VerificationRequest;
import com.myce.notification.service.MailSendService;
import com.myce.notification.service.MessageGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/notifications/mail")
@RequiredArgsConstructor
@Slf4j
public class InternalMailSendController {

    private final MailSendService mailSendService;
    private final MessageGenerateService messageGenerateService;

    @PostMapping("")
    public void sendMail(@RequestBody EmailSendRequest req) {
        mailSendService.sendMail(req);
    }

    @PostMapping("/multi")
    public void sendMailToMultiple(@RequestBody MultiEmailSendRequest req) {
        mailSendService.sendMailToMultiple(req);
    }

    @PostMapping("/support")
    public void sendSupportMail(@RequestBody EmailSendRequest req) {
        mailSendService.sendSupportMail(req);
    }

    @PostMapping("/verification")
    public void sendVerificationMail(@RequestBody VerificationRequest req) {
        messageGenerateService.getMessageForVerification(req);
    }

    @PostMapping("/reset-password")
    public void sendResetPwMail(@RequestBody ResetRequest req) {
        messageGenerateService.getMessageForResetPassword(req);
    }

    @PostMapping("/reservation-confirm")
    public void sendConfirmMail(@RequestBody ReservationConfirmRequest req) {
        messageGenerateService.getMessageForReservationConfirmation(req);
    }
}
