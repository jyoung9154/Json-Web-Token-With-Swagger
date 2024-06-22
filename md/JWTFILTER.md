# JWT 인증 필터

이 프로젝트에는 JWT(JSON Web Token) 인증을 처리하는 `JwtAuthFilter` 클래스가 포함되어 있습니다. 이 필터는 매 요청 시 JWT 토큰의 유효성을 검사하고, 인증된 사용자 정보를 `SecurityContextHolder`에 설정합니다.

## 주요 기능

1. **JWT 토큰 확인**: 요청 헤더의 `Authorization` 필드에서 JWT 토큰을 추출합니다.
2. **JWT 토큰 검증**: `JwtUtil` 클래스를 사용하여 JWT 토큰의 유효성을 검사합니다.
3. **사용자 정보 로드**: 유효한 토큰에서 사용자 ID를 추출하고, `UserService`를 사용하여 사용자 정보를 로드합니다.
4. **인증 정보 설정**: 로드된 사용자 정보를 사용하여 `UsernamePasswordAuthenticationToken`을 생성하고, `SecurityContextHolder`에 설정합니다.
5. **예외 처리**: 토큰 검증 또는 사용자 정보 로드 중에 발생한 예외를 처리하고, 적절한 HTTP 응답을 반환합니다.

## 코드 설명

```java
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    /* 기능 객체 */
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /* 매 요청시 JWT 인증 권한 확인 */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        try {
            /* 토큰 확인 */
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                /* 토큰 검증*/
                if (jwtUtil.validateToken(token)){
                    String userId = jwtUtil.extractUserId(token);
                    User authUser = userService.loadUserByUserId(userId);

                    /* 사용자 확인 */
                    if(authUser != null){
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(authUser, null);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception e){
            /* Filter에서 Exception 발생 시 */
            setErrorResponse(response, e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, Exception e) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorCode code;

        response.setHeader("Content-type", "application/json");
        response.setCharacterEncoding("utf-8");
        try {
            /* 토큰 있을 때 */
            code = ((ExceptionResponse) e).getErrorCode();
            response.setStatus(code.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ExceptionEntity.toResponseEntity(code).getBody()));
        } catch (Exception e2) {
            /* 토큰 없을 때 */
            code = new ExceptionResponse(ErrorCode.JWT_ILLEGAL).getErrorCode();
            response.setStatus(code.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ExceptionEntity.toResponseEntity(code).getBody()));
        }
    }
}
