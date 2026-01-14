package com.myce.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myce.global.dto.CustomUserDetails;
import com.myce.global.dto.type.LoginType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String GATEWAY_AUTH_VALUE;
    private final String INTERNAL_AUTH_VALUE;

    /**
     * 요청의 내부 인증 헤더를 검증하여 내부/게이트웨이 호출을 식별하고,
     * 필요한 경우 요청을 인증하여 Spring Security 컨텍스트에 저장하거나 401 응답을 반환한다.
     *
     * <p>동작 요약:
     * - 헤더 `InternalHeaderKey.INTERNAL_AUTH` 값이 `GATEWAY_AUTH_VALUE` 또는 `INTERNAL_AUTH_VALUE`와 일치하지 않으면 401 상태를 반환하고 처리 중단.
     * - 내부 사용자 정보(역할, 로그인 타입, 멤버 ID) 헤더가 하나라도 누락되면 인증을 수행하지 않고 필터 체인을 계속 진행.
     * - 사용자 정보가 모두 존재하면 CustomUserDetails를 생성하여 인증 토큰을 SecurityContext에 설정한 후 필터 체인을 계속 진행.
     * - 사용자 조회 중 UsernameNotFoundException이 발생하면 401 상태를 반환하고 처리 중단.
     *
     * @throws ServletException 서블릿 처리 중 예외 발생 시
     * @throws IOException 입출력 처리 중 예외 발생 시
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        log.debug("[JwtAuthenticationFilter] Input uri={}, method={}", uri, method);

        String authValue = request.getHeader(InternalHeaderKey.INTERNAL_AUTH);

        if (authValue == null || (!authValue.equals(GATEWAY_AUTH_VALUE) && !authValue.equals(INTERNAL_AUTH_VALUE))) {
            log.info("Not exist auth value. authValue={}", authValue);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String role = request.getHeader(InternalHeaderKey.INTERNAL_ROLE);
        String loginTypeStr = request.getHeader(InternalHeaderKey.INTERNAL_LOGIN_TYPE);
        String memberIdStr = request.getHeader(InternalHeaderKey.INTERNAL_MEMBER_ID);

        if (role == null || loginTypeStr == null || memberIdStr == null) {
            log.info("Not exist user info. role={}, loginType={}, memberId={}", role, loginTypeStr, memberIdStr);
            filterChain.doFilter(request, response);
            return;
        }

        // 사용자 정보 확인
        LoginType loginType = LoginType.fromString(loginTypeStr);
        Long memberId = Long.valueOf(memberIdStr);

        log.info("[AuthenticationFilter] JWT find userDetails by memberId: {}, loginType: {}",
                memberIdStr, loginTypeStr);
        CustomUserDetails userDetails;
        try {
            userDetails = CustomUserDetails.builder()
                    .loginType(loginType)
                    .memberId(memberId)
                    .role(role)
                    .build();
        } catch (UsernameNotFoundException e) {
            log.error("[AuthenticationFilter] failed to find user details by memberId: {}", memberIdStr, e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        log.debug("[AuthenticationFilter] Success to find userDetail: {}", userDetails);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 다음 필터로
        filterChain.doFilter(request, response);
    }


    /**
     * 응답에 HTTP 401 상태와 JSON 형태의 에러 코드를 설정하고 응답 본문을 작성합니다.
     *
     * @param response HTTP 응답 객체
     * @param code     응답 본문의 `code` 필드에 들어갈 에러 코드 문자열
     * @throws IOException 응답 출력 시 입출력 오류가 발생하면 전달됩니다
     */
    private void setErrorResponse(HttpServletResponse response, String code) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, String> body = Map.of("code", code);
        log.info("[JwtAuthenticationFilter] Set error response: {}", body);
        new ObjectMapper().writeValue(response.getWriter(), body);
    }
}