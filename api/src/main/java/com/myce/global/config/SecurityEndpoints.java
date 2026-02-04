package com.myce.global.config;

// Spring Security에서 인증 없이 접근 가능한 엔드포인트를 정의하는 설정 클래스
public final class SecurityEndpoints {

    private SecurityEndpoints() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ===== POST 요청 허용 엔드포인트 =====
    public static final String[] POST_PERMIT_ALL = {
    };

    // ===== GET 요청 허용 엔드포인트 =====
    public static final String[] GET_PERMIT_ALL = {
    };

    // ===== PATCH 요청 허용 엔드포인트 =====
    public static final String[] PATCH_PERMIT_ALL = {
    };

    // ===== DELETE 요청 허용 엔드포인트 =====
    public static final String[] DELETE_PERMIT_ALL = {

    };

    // ===== 기타 요청 허용 엔드포인트 (Swagger, Actuator, WebSocket 등) =====
    public static final String[] ETC_PERMIT_ALL = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/auth-docs/login",
            "/actuator/**",
            "/ws/**",
            "/images/**",
            "/api/login/oauth2/code/**",
            "/api/oauth2/**",
            "/internal/**"
    };
}
