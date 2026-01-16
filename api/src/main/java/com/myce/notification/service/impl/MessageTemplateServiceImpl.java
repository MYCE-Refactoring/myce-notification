package com.myce.notification.service.impl;

import com.myce.notification.dto.request.SendGetMessageRequest;
import com.myce.notification.exception.CustomErrorCode;
import com.myce.notification.exception.CustomException;
import com.myce.notification.dto.message.MessageTemplateListResponse;
import com.myce.notification.dto.message.MessageTemplateResponse;
import com.myce.notification.dto.message.UpdateMessageTemplateRequest;
import com.myce.notification.entity.MessageTemplateSetting;
import com.myce.notification.entity.type.ChannelType;
import com.myce.notification.repository.MessageTemplateSettingRepository;
import com.myce.notification.service.mapper.MessageTemplateMapper;
import com.myce.notification.service.MessageCommonService;
import com.myce.notification.service.MessageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MessageTemplateServiceImpl implements MessageTemplateService {

    private final MessageTemplateSettingRepository templateSettingRepository;
    private final MessageTemplateMapper messageTemplateMapper;
    private final MessageCommonService messageCommonService;

    @Override
    public MessageTemplateListResponse getAllMessageTemplates(int page, String keyword) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MessageTemplateSetting> templates = keyword.isBlank() ?
                templateSettingRepository.findAll(pageable) :
                templateSettingRepository.findAllByNameContains(keyword, pageable);
        return messageTemplateMapper.toTemplatesResponse(templates);
    }

    @Override
    public MessageTemplateResponse getMessageTemplateById(long id) {
        MessageTemplateSetting templateSetting = templateSettingRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

        SendGetMessageRequest req = SendGetMessageRequest.builder().
                content(templateSetting.getContent()).
                code(templateSetting.getCode()).
                isUseImage(templateSetting.isUseImage()).
                context(new Context()).
                build();

        String template = "";
        if(templateSetting.getChannelType().equals(ChannelType.EMAIL)) {
            template = messageCommonService.getFullMessage(req);
        }
        return messageTemplateMapper.toTemplateResponse(templateSetting, template);
    }

    @Override
    public MessageTemplateResponse updateMessageTemplate(long id, UpdateMessageTemplateRequest request) {
        MessageTemplateSetting templateSetting = templateSettingRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

        String name = request.getName();
        String subject = request.getSubject();
        String content = request.getContent();
        templateSetting.updateTemplate(name, subject, content);

        templateSettingRepository.save(templateSetting);

        SendGetMessageRequest req = SendGetMessageRequest.builder().
                content(content).
                code(templateSetting.getCode()).
                isUseImage(templateSetting.isUseImage()).
                context(new Context()).
                build();

        String template = "";
        if(templateSetting.getChannelType().equals(ChannelType.EMAIL)) {
            template = messageCommonService.getFullMessage(req);
        }
        return messageTemplateMapper.toTemplateResponse(templateSetting, template);
    }
}
