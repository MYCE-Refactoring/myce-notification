package com.myce.notification.document.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public enum AdvertisementStatus {
    PENDING_APPROVAL("승인 대기"),
    PENDING_PAYMENT("결제 대기"),
    PENDING_PUBLISH("게시 대기"),
    PENDING_CANCEL("취소 대기"),
    PUBLISHED("게시 중"),
    COMPLETED("종료됨"),
    REJECTED("승인 거절"),
    CANCELLED("취소 완료");

    private final String label;


    public static AdvertisementStatus fromString(String text) {
        for (AdvertisementStatus t : AdvertisementStatus.values()) {
            if (t.name().equalsIgnoreCase(text))return t;
        }
        return null;
    }

    public static final List<AdvertisementStatus> COMPLETED_STATUSES = List.of(
            COMPLETED, CANCELLED
    );

    public static final List<AdvertisementStatus> EXPIRED_STATUSES = List.of(
            REJECTED, CANCELLED
    );

    public static final List<AdvertisementStatus> ACTIVE_STATUSES = List.of(
            PUBLISHED, PENDING_CANCEL
    );

    public static final List<AdvertisementStatus> ADMIN_VIEWABLE_STATUSES = List.of(
            PENDING_PUBLISH, PUBLISHED, PENDING_CANCEL, COMPLETED, CANCELLED
    );
}
