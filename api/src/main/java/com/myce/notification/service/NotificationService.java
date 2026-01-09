package com.myce.notification.service;


import com.myce.notification.dto.response.NotificationResponse;
import com.myce.notification.dto.response.NotificationResponseList;
import com.myce.notification.dto.response.PageResponse;

public interface NotificationService {

    PageResponse<NotificationResponse> getNotificationsByMemberId(Long memberId, int page);
    void markAsRead(String notificationId, Long memberId);
    void markAllAsRead(Long memberId);
}