package com.myce.notification.service.impl;

import com.myce.global.dto.type.LoginType;
import com.myce.notification.common.ExpoAdminPermission;
import com.myce.notification.exception.CustomErrorCode;
import com.myce.notification.exception.CustomException;
import com.myce.notification.restclient.component.EnsureRequestComponent;
import com.myce.notification.service.MailAdminDetailService;
import com.myce.notification.dto.email.ExpoAdminEmailDetailResponse;
import com.myce.notification.dto.email.ExpoAdminEmailResponse;
import com.myce.notification.repository.EmailLogRepository;
import com.myce.notification.service.mapper.ExpoAdminEmailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailAdminDetailServiceImpl implements MailAdminDetailService {

    private final EmailLogRepository emailLogRepository;
    private final ExpoAdminEmailMapper mapper;
    private final EnsureRequestComponent ensureRequestComponent;

    @Override
    public Page<ExpoAdminEmailResponse> getMyMails(Long expoId,
                                                   Long memberId,
                                                   LoginType loginType,
                                                   String keyword,
                                                   Pageable pageable) {

        ensureRequestComponent.viewableEnsureRequest(
                expoId, memberId, loginType, ExpoAdminPermission.EMAIL_LOG_VIEW
        );

        boolean hasKeyword = keyword != null && !keyword.isBlank();

        if(!hasKeyword){
            return emailLogRepository
                    .findByExpoId(expoId, pageable)
                    .map(mapper::toDto);
        }

        String safeKeyword = java.util.regex.Pattern.quote(keyword.trim());
        return emailLogRepository
                .searchByExpoIdAndKeyword(expoId,safeKeyword,pageable)
                .map(mapper::toDto);
    }

    @Override
    public ExpoAdminEmailDetailResponse getMyMailDetail(Long expoId, Long memberId, LoginType loginType, String emailId) {
        //코어
        ensureRequestComponent.viewableEnsureRequest(
                expoId, memberId, loginType, ExpoAdminPermission.EMAIL_LOG_VIEW
        );
        return emailLogRepository.findById(emailId)
                .map(mapper::toDetailDto)
                .orElseThrow(() -> new CustomException( CustomErrorCode.INVALID_EMAIL_LOG));
    }
}
