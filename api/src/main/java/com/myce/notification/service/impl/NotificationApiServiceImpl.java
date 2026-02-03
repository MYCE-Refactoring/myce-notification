package com.myce.notification.service.impl;

import com.myce.notification.dto.response.NotificationResponse;
import com.myce.notification.dto.response.PageResponse;
import com.myce.notification.repository.NotificationRepository;
import com.myce.notification.service.NotificationApiService;
import com.myce.notification.service.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationApiServiceImpl implements NotificationApiService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public PageResponse<NotificationResponse> getNotificationsByMemberId(
            Long memberId, int page
    ) {
        Pageable pageable = PageRequest.of(
                page,
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return PageResponse.from(
                notificationRepository.findByMemberId(memberId, pageable)
                        .map(notificationMapper::toResponse)
        );

    }
    @Override
    public void markAsRead(String notificationId, Long memberId) {
        // MongoDB @Update를 사용하여 원자적 업데이트 수행
        // Query 조건에 memberId도 포함하여 권한 체크와 업데이트를 한번에 처리
        notificationRepository.markAsRead(notificationId, memberId, LocalDateTime.now());
        log.info("알림 읽음 처리 완료 - 알림 ID: {}, 회원 ID: {}", notificationId, memberId);
    }

    @Override
    public void markAllAsRead(Long memberId) {
        // 해당 회원의 모든 읽지 않은 알림을 읽음 처리
        notificationRepository.markAllAsReadByMemberId(memberId, LocalDateTime.now());
        log.info("모든 알림 읽음 처리 완료 - 회원 ID: {}", memberId);
    }

}
