package toy.project.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Jwt User_Controller", description = "회원가입/로그인 API")
@RequestMapping("/jwt")
public class JwtUserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "회원가입", description = "회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "409", description = "해당 아이디가 이미 존재합니다.", content = @Content)
    })
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(signupRequest).getBody());
    }

    @Operation(summary = "로그인", description = "사용자 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "로그인 성공", content = @Content(schema = @Schema(implementation = JwtToken.class))),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 다시 확인해주세요.", content = @Content),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다.", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.login(loginRequest).getBody());
    }

    @Operation(summary = "JWT 토큰 호출", description = "JWT가 정상적으로 동작하는지 확인합니다.")
    @GetMapping("/token")
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
