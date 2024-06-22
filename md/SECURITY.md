# Spring Security 설정

## 개요
이 프로젝트는 Spring Security를 사용하여 웹 애플리케이션의 보안을 관리합니다. 주요 기능은 다음과 같습니다.

1. Iframe 랜더링 방지
2. 기본 인증 비활성화
3. CSRF 공격 방지
4. CORS 비활성화
5. 접근 권한 설정
6. 세션 관리 정책 설정
7. JWT 인증 필터 적용

## 주요 기능

1. **Iframe 랜더링 방지**
    - `headers(headers-> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))` 설정을 통해 Iframe 랜더링을 동일 출처로 제한합니다.

2. **기본 인증 비활성화**
    - `httpBasic().disable()` 설정을 통해 기본 인증을 비활성화합니다.

3. **CSRF 공격 방지**
    - `csrf().disable()` 설정을 통해 CSRF 공격을 방지합니다.

4. **CORS 비활성화**
    - `cors().disable()` 설정을 통해 CORS를 비활성화합니다.

5. **접근 권한 설정**
    - `authorizeRequests()` 설정을 통해 특정 URL에 대한 접근 권한을 설정합니다.
    - `/szs/signup`, `/szs/login`, `/3o3/**`, `/api-docs/**` 경로는 모두 허용합니다.
    - H2 콘솔 접근도 허용합니다.

6. **세션 관리 정책 설정**
    - `sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)` 설정을 통해 세션 관리 정책을 무상태(stateless)로 설정합니다.

7. **JWT 인증 필터 적용**
    - `addFilterBefore(new JwtAuthFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class)` 설정을 통해 JWT 인증 필터를 적용합니다.
