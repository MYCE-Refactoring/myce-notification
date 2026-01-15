package com.myce.notification.controller;

import com.myce.notification.dto.request.*;
import com.myce.notification.service.EmailSendService;
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
public class InternalMessageController {

    private final EmailSendService emailSendService;

    @PostMapping("")
    public void sendMail(@RequestBody EmailSendRequest req) {
        emailSendService.sendMail(req);
    }

    @PostMapping("/multi")
    public void sendMailToMultiple(@RequestBody MultiEmailSendRequest req) {
        emailSendService.sendMailToMultiple(req);
    }

    @PostMapping("/support")
    public void sendSupportMail(@RequestBody EmailSendRequest req) {
        emailSendService.sendSupportMail(req);
    }
}
