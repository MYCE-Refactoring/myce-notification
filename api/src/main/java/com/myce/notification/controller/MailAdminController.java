package com.myce.notification.controller;

import com.myce.global.dto.CustomUserDetails;
import com.myce.global.dto.type.LoginType;
import com.myce.notification.dto.email.ExpoAdminEmailDetailResponse;
import com.myce.notification.dto.email.ExpoAdminEmailRequest;
import com.myce.notification.dto.email.ExpoAdminEmailResponse;
import com.myce.notification.dto.response.PageResponse;
import com.myce.notification.service.MailAdminDetailService;
import com.myce.notification.service.MailAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/{expoId}/mail-admin")
public class MailAdminController {

    private final MailAdminService mailAdminService;
    private final MailAdminDetailService mailAdminDetailService;

    @PostMapping
    public ResponseEntity<Void> sendEmail(
            @PathVariable Long expoId,
            @RequestBody @Valid ExpoAdminEmailRequest dto,
            @RequestParam(required = false) String entranceStatus,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String reservationCode,
            @RequestParam(required = false) String ticketName,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long memberId = customUserDetails.getMemberId();
        LoginType loginType = customUserDetails.getLoginType();
        mailAdminService.sendMail(memberId,loginType,expoId,dto,entranceStatus,name,phone,reservationCode,ticketName);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<ExpoAdminEmailResponse>> getMyEmails(
            @PathVariable Long expoId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long memberId = customUserDetails.getMemberId();
        LoginType loginType = customUserDetails.getLoginType();

        Sort.Direction direction = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page,size,Sort.by(direction,"createdAt"));

        Page<ExpoAdminEmailResponse> result = mailAdminDetailService.getMyMails(expoId, memberId, loginType, keyword, pageable);

        return ResponseEntity.ok(PageResponse.from(result));
    } // PageResponse로 변환

    @GetMapping("/{emailId}")
    public ResponseEntity<ExpoAdminEmailDetailResponse> getMyEmailDetail(
            @PathVariable Long expoId,
            @PathVariable String emailId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long memberId = customUserDetails.getMemberId();
        LoginType loginType = customUserDetails.getLoginType();

        ExpoAdminEmailDetailResponse response = mailAdminDetailService.getMyMailDetail(expoId,memberId,loginType,emailId);
        return ResponseEntity.ok(response);
    }
}
