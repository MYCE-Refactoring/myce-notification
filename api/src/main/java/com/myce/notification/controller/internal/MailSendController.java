package com.myce.notification.controller.internal;


import com.myce.notification.dto.email.EmailSendRequest;
import com.myce.notification.dto.email.MultiEmailSendRequest;
import com.myce.notification.dto.message.ReservationConfirmRequest;
import com.myce.notification.dto.message.ResetRequest;
import com.myce.notification.dto.message.VerificationRequest;
import com.myce.notification.service.internal.MailSendService;
import com.myce.notification.service.internal.MessageGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/notifications/mail")
@RequiredArgsConstructor
@Slf4j
public class MailSendController {

    private final MailSendService mailSendService;
    private final MessageGenerateService messageGenerateService;

    @PostMapping("")
    public ResponseEntity<Void> sendMail(@RequestBody EmailSendRequest req) {
        mailSendService.sendMail(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/multi")
    public ResponseEntity<Void> sendMailToMultiple(@RequestBody MultiEmailSendRequest req) {
        mailSendService.sendMailToMultiple(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/support")
    public ResponseEntity<Void> sendSupportMail(@RequestBody EmailSendRequest req) {
        mailSendService.sendSupportMail(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verification")
    public ResponseEntity<Void> sendVerificationMail(@RequestBody VerificationRequest req) {
        messageGenerateService.getMessageForVerification(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> sendResetPwMail(@RequestBody ResetRequest req) {
        messageGenerateService.getMessageForResetPassword(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reservation-confirm")
    public ResponseEntity<Void> sendConfirmMail(@RequestBody ReservationConfirmRequest req) {
        messageGenerateService.getMessageForReservationConfirmation(req);
        return ResponseEntity.noContent().build();
    }
}
