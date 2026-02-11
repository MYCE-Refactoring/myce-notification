package com.myce.notification.service.impl;

import com.myce.global.dto.type.LoginType;
import com.myce.notification.common.ExpoAdminPermission;
import com.myce.notification.dto.email.MailSendContextResponse;
import com.myce.notification.exception.CustomErrorCode;
import com.myce.notification.exception.CustomException;
import com.myce.notification.client.component.EnsureRequestComponent;
import com.myce.notification.service.MailAdminService;
import com.myce.notification.document.EmailLog;
import com.myce.notification.dto.email.ExpoAdminEmailRequest;
import com.myce.notification.repository.EmailLogRepository;
import com.myce.notification.service.mapper.ExpoAdminEmailMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailAdminServiceImpl implements MailAdminService {

    private final EmailLogRepository emailLogRepository;
    private final SpringTemplateEngine templateEngine;
    private final ExpoAdminEmailMapper mapper;
    private final JavaMailSender mailSender;
    private final EnsureRequestComponent ensureRequestComponent;


    //TODO : 추후 링크 교체, 또는 @Value로 값 주입
    private final String TERMS_URL= "http://www.myce.live";
    private final String REFUND_URL = "http://www.myce.live";
    private final String PRIVACY_URL = "http://www.myce.live";

    @Override
    @Transactional
    public void sendMail(Long memberId,
                         LoginType loginType,
                         Long expoId,
                         ExpoAdminEmailRequest dto,
                         String entranceStatus,
                         String name,
                         String phone,
                         String reservationCode,
                         String ticketName) {

        ensureRequestComponent.editableEnsureRequest(expoId, memberId, loginType, ExpoAdminPermission.RESERVER_LIST_VIEW);

        MailSendContextResponse response = ensureRequestComponent.mailSendContextRequest(
                expoId, entranceStatus, name, phone, reservationCode, ticketName
        );

        log.info("메일 발송 컨텍스트 조회 성공: expoId={}, response={}", expoId, response);


        String html = renderEmailHtml(dto, response);

        List<EmailLog.RecipientInfo> recipientInfos;
        if (dto.isSelectAllMatching()) {
            recipientInfos = response.getRecipientInfos().stream()
                    .map(r -> new EmailLog.RecipientInfo(r.getEmail(), r.getName()))
                    .toList();
        } else {
            recipientInfos = dto.getRecipientInfos();
        }

        List<String> emails = recipientInfos.stream()
                        .map(EmailLog.RecipientInfo::getEmail)
                        .toList();

        sendMailToMultiple(emails, dto.getSubject(), html); //TODO: 추후 대량 이메일 전송 대비 배치 도입 고려

        emailLogRepository.save(mapper.toDocument(expoId,dto,recipientInfos));
    }

    private String renderEmailHtml(ExpoAdminEmailRequest req, MailSendContextResponse response){

        String expoName = response.getExpoName();
        String contactPhone = response.getContactPhone();
        String contactEmail = response.getContactEmail();

        String contentHtml = Optional.ofNullable(req.getContent())
                .map(s -> s.replace("\r\n", "\n").replace("\r", "\n").replace("\n", "<br/>"))
                .orElse("");

        Context ctx = new Context(Locale.KOREA);
        ctx.setVariable("preheader", toPreheader(req.getContent(), 80));
        ctx.setVariable("subject", req.getSubject());
        ctx.setVariable("content", contentHtml);
        ctx.setVariable("expoName",expoName);
        ctx.setVariable("contactPhone",contactPhone);
        ctx.setVariable("contactEmail",contactEmail);
        ctx.setVariable("termsUrl", TERMS_URL);
        ctx.setVariable("refundUrl", REFUND_URL);
        ctx.setVariable("privacyUrl", PRIVACY_URL);

        return templateEngine.process("mail/mail-basic",ctx);
    }

    private String toPreheader(String html, int maxLen) {
        if (html == null) return "";
        String text = html.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
        return text.length() > maxLen ? text.substring(0, maxLen) + "…" : text;
    }

    public void sendMailToMultiple(List<String> recipients, String subject, String content){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom("noreply@myce.cloud", "MYCE");


            String[] toArray = recipients .toArray(new String[0]);
            messageHelper.setBcc(toArray);

            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

            mailSender.send(mimeMessage);
            log.info("Email sent successfully. from=noreply@myce.cloud, recipients={}명, subject={}", recipients.size(), subject);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException( CustomErrorCode.MAIL_SEND_FAIL);
        }
    }
}