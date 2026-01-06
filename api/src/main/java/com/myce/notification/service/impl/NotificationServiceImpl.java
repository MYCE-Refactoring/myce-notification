package com.myce.notification.service.impl;

import com.myce.notification.document.type.AdvertisementStatus;
import com.myce.notification.document.type.ExpoStatus;
import com.myce.notification.document.Notification;
import com.myce.notification.dto.NotificationResponseList;
import com.myce.notification.repository.NotificationRepository;
import com.myce.notification.service.NotificationService;
import com.myce.notification.service.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
//    private final SseService sseService;
//    private final MessageTemplateSettingRepository messageTemplateSettingRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponseList getNotificationsByMemberId(
            Long memberId, int page
    ) {
        Pageable Pageable = PageRequest.of(
                page,
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        Page<Notification> pages = notificationRepository.findByMemberId(memberId, Pageable);
        return notificationMapper.toResponseList(pages);
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

    private String getStatusDisplayName(ExpoStatus status) {
        switch (status) {
            case PENDING_APPROVAL:
                return "승인 대기";
            case PENDING_PAYMENT:
                return "결제 대기";
            case PENDING_PUBLISH:
                return "게시 대기";
            case PENDING_CANCEL:
                return "취소 대기";
            case PUBLISHED:
                return "게시 중";
            case PUBLISH_ENDED:
                return "게시 종료";
            case SETTLEMENT_REQUESTED:
                return "정산 요청";
            case COMPLETED:
                return "종료됨";
            case REJECTED:
                return "승인 거절";
            case CANCELLED:
                return "취소 완료";
            default:
                return status.name();
        }
    }

    private String getAdStatusDisplayName(AdvertisementStatus status) {
        switch (status) {
            case PENDING_APPROVAL:
                return "승인 대기";
            case PENDING_PAYMENT:
                return "결제 대기";
            case PENDING_PUBLISH:
                return "게시 대기";
            case PENDING_CANCEL:
                return "취소 대기";
            case PUBLISHED:
                return "게시 중";
            case COMPLETED:
                return "종료됨";
            case REJECTED:
                return "승인 거절";
            case CANCELLED:
                return "취소 완료";
            default:
                return status.name();
        }
    }


}
