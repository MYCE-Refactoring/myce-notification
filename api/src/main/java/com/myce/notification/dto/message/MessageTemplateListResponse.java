package com.myce.notification.dto.message;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MessageTemplateListResponse {
    int currentPage;
    int totalPage;
    List<MessageSummaryResponse> templates;

    public MessageTemplateListResponse(int currentPage, int totalPage) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.templates = new ArrayList<>();
    }

    public void addMessageTemplate(MessageSummaryResponse template) {
        this.templates.add(template);
    }
}
