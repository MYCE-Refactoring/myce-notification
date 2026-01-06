package com.myce.notification.service;


import com.myce.notification.dto.NotificationResponseList;

public interface NotificationService {

    NotificationResponseList getNotificationsByMemberId(Long memberId, int page);
    void markAsRead(String notificationId, Long memberId);
    void markAllAsRead(Long memberId);
}