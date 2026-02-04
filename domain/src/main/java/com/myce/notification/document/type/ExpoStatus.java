package com.myce.notification.document.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ExpoStatus {
  
    PENDING_APPROVAL("승인 대기"),
    PENDING_PAYMENT("결제 대기"),
    PENDING_PUBLISH("게시 대기"),
    PENDING_CANCEL("취소 대기"),
    PUBLISHED("게시 중"),
    PUBLISH_ENDED("게시 종료"),
    SETTLEMENT_REQUESTED("정산 요청"),
    COMPLETED("종료됨"),
    REJECTED("승인 거절"),
    CANCELLED("취소 완료");

    private final String label;
}