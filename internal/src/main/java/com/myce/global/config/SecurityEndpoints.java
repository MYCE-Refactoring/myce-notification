package com.myce.global.config;

// Spring Security에서 인증 없이 접근 가능한 엔드포인트를 정의하는 설정 클래스
public final class SecurityEndpoints {

    private SecurityEndpoints() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ===== POST 요청 허용 엔드포인트 =====
    public static final String[] POST_PERMIT_ALL = {
            "/internal/notifications/**",
    };

    // ===== GET 요청 허용 엔드포인트 =====
    public static final String[] GET_PERMIT_ALL = {
            "/internal/notifications/**",
    };

    // ===== PATCH 요청 허용 엔드포인트 =====
    public static final String[] PATCH_PERMIT_ALL = {
            "/internal/notifications/**",
    };

    // ===== DELETE 요청 허용 엔드포인트 =====
    public static final String[] DELETE_PERMIT_ALL = {
            "/internal/notifications/**",

    };

    // ===== 기타 요청 허용 엔드포인트 (Swagger, Actuator, WebSocket 등) =====
    public static final String[] ETC_PERMIT_ALL = {
            "/internal/notifications/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/actuator/**",
            "/images/**",
    };
}
