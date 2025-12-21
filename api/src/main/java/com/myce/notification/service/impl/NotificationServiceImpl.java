package com.myce.notification.service.impl;

import com.myce.notification.document.Notification;
import com.myce.notification.dto.NotificationResponse;
import com.myce.notification.repository.NotificationRepository;
import com.myce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationResponse> getNotificationsByMemberId(Long memberId) {
        try {
            // 최신순으로 정렬하여 조회
            Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
            List<Notification> notifications = notificationRepository.findByMemberId(memberId, sort);

            return notifications.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("알림 목록 조회 실패 - 회원 ID: {}, 오류: {}", memberId, e.getMessage(), e);
            return List.of();
        }
    }

    private NotificationResponse convertToResponse(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .type(notification.getType())
                .targetType(notification.getTargetType())
                .targetId(notification.getTargetId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}