package toy.project.jwt.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import toy.project.jwt.domain.User;
import toy.project.jwt.exception.ErrorCode;
import toy.project.jwt.exception.ExceptionEntity;
import toy.project.jwt.exception.ExceptionResponse;
import toy.project.jwt.service.UserService;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    /* 기능 객체 */
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /* 매 요청시 JWT 인증 권한 확인 */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String authHeader = request.getHeader("Authorization");
        String url = request.getServletPath();
        try {
            /* 토큰 소유 시 권한이 필요없는 URL 접속 할 경우 filter 제외 */
            if(url.equals("/jwt/login") || url.equals("/jwt/signup")) {
                filterChain.doFilter(request, response);
                return;
            }
            /* 토큰 확인 */
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                /* 요청한 토큰이 Refresh 토큰이면 접근 불가 */
                String role = (String) jwtUtil.extractAllClaims(token).get("ROLE");
                if(role == null || !role.equals("access")){
                    throw  new ExceptionResponse(ErrorCode.JWT_REFRESH);
                }

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
        ErrorCode errorCode;

        response.setHeader("Content-type", "application/json");
        response.setCharacterEncoding("utf-8");

        try {
            errorCode = getErrorCode(e);
            response.setStatus(errorCode.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ExceptionEntity.toResponseEntity(errorCode, "").getBody()));
        } catch (IOException ex) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(objectMapper.writeValueAsString(ExceptionEntity.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR, "").getBody()));
        }
    }

    private ErrorCode getErrorCode(Exception e) {
        if (e instanceof ExceptionResponse) {
            return ((ExceptionResponse) e).getErrorCode();
            /* 토큰 만료 */
        } else if (e instanceof ExpiredJwtException) {
            return new ExceptionResponse(ErrorCode.JWT_EXPIRED).getErrorCode();
            /* 토큰 이슈 */
        }else if (e instanceof MalformedJwtException  | e instanceof SignatureException | e instanceof UnsupportedJwtException) {
            return new ExceptionResponse(ErrorCode.JWT_MALFORMED).getErrorCode();
            /* 토큰 없음 */
        } else if (e instanceof IllegalArgumentException | e instanceof ServletException) {
            return new ExceptionResponse(ErrorCode.JWT_ILLEGAL).getErrorCode();
        } else {
            return new ExceptionResponse(ErrorCode.JWT_UNAUTH).getErrorCode();
        }
    }
}
