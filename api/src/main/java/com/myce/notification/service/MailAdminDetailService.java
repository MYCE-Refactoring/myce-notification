package com.myce.notification.service;

import com.myce.global.dto.type.LoginType;
import com.myce.notification.dto.email.ExpoAdminEmailDetailResponse;
import com.myce.notification.dto.email.ExpoAdminEmailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MailAdminDetailService {
    Page<ExpoAdminEmailResponse> getMyMails(Long expoId,
                                            Long memberId,
                                            LoginType loginType,
                                            String keyword,
                                            Pageable pageable);

    ExpoAdminEmailDetailResponse getMyMailDetail(Long expoId, Long memberId, LoginType loginType, String emailId);
}