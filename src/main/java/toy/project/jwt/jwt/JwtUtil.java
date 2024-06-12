package toy.project.jwt.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.stereotype.Component;
import toy.project.jwt.exception.ErrorCode;
import toy.project.jwt.exception.ExceptionResponse;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.expire-time}")
    private long EXPIRE_TIME;

    @Value("${jwt.refresh-time}")
    private long REFRESH_TIME;

    /* Access 토큰 생성 */
    public String createAccessToken(String userId, String type) {
        return createToken(userId, EXPIRE_TIME, type);
    }

    /* Refresh 토큰 생성 */
    public String createRefreshToken(String userId) {
        return createToken(userId, REFRESH_TIME,"");
    }

    /* 토큰 생성 */
    public String createToken(String userId, long time, String type) {
        Claims claims = Jwts.claims();
        if(type.equals("access")) {
            claims.put("ROLE", "access");
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /* 토큰 사용자 ID 추출 */
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    /* 토큰 검증 기능 */
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
            /* 토큰 만료 */
            throw new ExceptionResponse(ErrorCode.JWT_EXPIRED);
        }catch (MalformedJwtException | SignatureException | UnsupportedJwtException e){
            /* 토큰 이슈 */
            throw new ExceptionResponse(ErrorCode.JWT_MALFORMED);
        }catch (BadJwtException | IllegalArgumentException e){
            /* 토큰 없음 */
            throw new ExceptionResponse(ErrorCode.JWT_ILLEGAL);
        }
    }
}
