package com.myce.notification.service;

import com.myce.notification.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getNotificationsByMemberId(Long memberId);

}