package com.myce.notification.service;

import com.myce.notification.document.type.NotificationTargetType;
import com.myce.notification.document.type.NotificationType;
import com.myce.notification.dto.AdStatusChangeCommand;
import com.myce.notification.dto.ExpoStatusChangeCommand;
import com.myce.notification.dto.NotificationResponseList;

import java.util.List;

public interface NotificationService {

    NotificationResponseList getNotificationsByMemberId(Long memberId, int page);
    void markAsRead(String notificationId, Long memberId);
    void markAllAsRead(Long memberId);
}