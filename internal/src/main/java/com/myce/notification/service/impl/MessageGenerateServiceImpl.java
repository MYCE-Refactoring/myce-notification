package com.myce.notification.service.impl;

import com.myce.notification.dto.request.SendGetMessageRequest;
import com.myce.notification.dto.request.mail.EmailSendRequest;
import com.myce.notification.dto.request.message.ReservationConfirmRequest;
import com.myce.notification.dto.request.message.ResetRequest;
import com.myce.notification.dto.request.message.VerificationRequest;
import com.myce.notification.entity.MessageTemplateSetting;
import com.myce.notification.entity.type.ChannelType;
import com.myce.notification.entity.type.MessageTemplateCode;
import com.myce.notification.entity.type.UserType;
import com.myce.notification.exception.CustomErrorCode;
import com.myce.notification.exception.CustomException;
import com.myce.notification.repository.MessageTemplateSettingRepository;
import com.myce.notification.service.MailSendService;
import com.myce.notification.service.MessageCommonService;
import com.myce.notification.service.MessageGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MessageGenerateServiceImpl implements MessageGenerateService {

    private final MessageTemplateSettingRepository messageTemplateSettingRepository;
    private final MessageCommonService messageCommonService;
    private final MailSendService mailSendService;

    @Override
    public void getMessageForVerification
            (VerificationRequest req) {

        String verificationName = req.getVerificationName();
        String code = req.getCode();
        String limitTime = req.getLimitTime();
        String email = req.getEmail();

        MessageTemplateSetting messageTemplate = messageTemplateSettingRepository
                .findByCodeAndChannelType(MessageTemplateCode.EMAIL_VERIFICATION, ChannelType.EMAIL)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("verificationName", verificationName);
        context.setVariable("limitTime", limitTime);

        SendGetMessageRequest request = SendGetMessageRequest.builder().
                content(messageTemplate.getContent()).
                code(messageTemplate.getCode()).
                isUseImage(messageTemplate.isUseImage()).
                context(context).
                build();

        String message = messageCommonService.getFullMessage(request);
        mailSendService.sendMail(new EmailSendRequest(email, messageTemplate.getSubject(), message));

    }

    @Override
    public void getMessageForResetPassword(ResetRequest req) {
        String password = req.getPassword();
        String email = req.getEmail();
        MessageTemplateSetting messageTemplate = messageTemplateSettingRepository
                .findByCodeAndChannelType(MessageTemplateCode.RESET_PASSWORD, ChannelType.EMAIL)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

        Context context = new Context();
        context.setVariable("tempPassword", password);

        SendGetMessageRequest request = SendGetMessageRequest.builder().
                content(messageTemplate.getContent()).
                code(messageTemplate.getCode()).
                isUseImage(messageTemplate.isUseImage()).
                context(context).
                build();

        String message = messageCommonService.getFullMessage(request);
        mailSendService.sendMail(new EmailSendRequest(email, messageTemplate.getSubject(), message));
    }

    @Override
    public void getMessageForReservationConfirmation(ReservationConfirmRequest req) {

        String name = req.getName();
        String email = req.getEmail();
        String expoTitle = req.getExpoTitle();
        String reservationCode = req.getReservationCode();
        Integer quantity = req.getQuantity();
        String paymentAmount = req.getPaymentAmount();
        UserType userType = req.getUserType();


        MessageTemplateSetting messageTemplate = messageTemplateSettingRepository
                .findByCodeAndChannelType(MessageTemplateCode.RESERVATION_CONFIRM, ChannelType.EMAIL)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("expoTitle", expoTitle);
        context.setVariable("reservationCode", reservationCode);
        context.setVariable("quantity", quantity);
        context.setVariable("paymentAmount", paymentAmount);
        context.setVariable("userType", userType);
        
        // 회원/비회원에 따른 안내 문구 설정
        String securityContent;
        if (userType == UserType.MEMBER) {
            securityContent = "• 예매 확인 및 QR 코드는 <a href='https://www.myce.live'>MYCE</a> 로그인 후, 마이페이지에서 확인 가능합니다<br>" +
                            "• 박람회 당일 QR 코드를 제시해주세요<br>" +
                            "• 문의사항이 있으시면 고객센터로 연락해주세요";
        } else {
            securityContent = "• 예매 확인 및 QR 코드는 <a href='https://www.myce.live/guest-reservation'>비회원 예매 확인</a>에서 확인 가능합니다<br>" +
                            "• 박람회 당일 QR 코드를 제시해주세요<br>" +
                            "• 문의사항이 있으시면 고객센터로 연락해주세요";
        }
        context.setVariable("securityContent", securityContent);

        SendGetMessageRequest request = SendGetMessageRequest.builder().
                content(messageTemplate.getContent()).
                code(messageTemplate.getCode()).
                isUseImage(messageTemplate.isUseImage()).
                context(context).
                build();

        String message = messageCommonService.getFullMessage(request);
        mailSendService.sendMail(new EmailSendRequest(email, messageTemplate.getSubject(), message));
    }
}
