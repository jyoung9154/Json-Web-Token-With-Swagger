package toy.project.jwt.service;

import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import toy.project.jwt.domain.User;
import toy.project.jwt.dto.JwtToken;
import toy.project.jwt.dto.LoginRequest;
import toy.project.jwt.dto.SignupRequest;

public interface UserService {

    /* 회원가입 기능 */
    @SneakyThrows
    String signup(SignupRequest user);

    /* 로그인 기능 */
    String login(LoginRequest loginRequest);

    /* DB 사용자 조회 기능 */
    User loadUserByUserId(String userId);

}
