package com.myce.notification.service;

import com.myce.notification.dto.message.MessageTemplateListResponse;
import com.myce.notification.dto.message.MessageTemplateResponse;
import com.myce.notification.dto.message.UpdateMessageTemplateRequest;

public interface MessageTemplateService {

    MessageTemplateListResponse getAllMessageTemplates(int page, String keyword);

    MessageTemplateResponse getMessageTemplateById(long id);

    MessageTemplateResponse updateMessageTemplate(long id, UpdateMessageTemplateRequest request);
}
