package toy.project.jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toy.project.jwt.domain.User;
import toy.project.jwt.dto.JwtToken;
import toy.project.jwt.dto.LoginRequest;
import toy.project.jwt.dto.SignupRequest;
import toy.project.jwt.exception.ErrorCode;
import toy.project.jwt.exception.ExceptionResponse;
import toy.project.jwt.jwt.JwtUtil;
import toy.project.jwt.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt")
public class JwtUserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /* 회원가입 기능 */
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(signupRequest).getBody());
    }

    /* 로그인 기능 */
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.login(loginRequest).getBody());
    }

    @GetMapping("/jwt")
    public String jwt(HttpServletRequest request) {
        /* 헤더에 토큰 받기 */
        String token = request.getHeader("Authorization").replace("Bearer ", "");

        /* 토큰 검증 후 Reurn */
        if (!jwtUtil.validateToken(token)) {
            throw new ExceptionResponse(ErrorCode.JWT_ILLEGAL);
        }

        return "정상토큰";
    }
}
