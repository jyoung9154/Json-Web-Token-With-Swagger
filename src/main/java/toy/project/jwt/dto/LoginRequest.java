package toy.project.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema( title = "로그인 요청 DTO")
public class LoginRequest {

    @Schema(description = "사용자 ID" ,example = "jyoung9154")
    String userId;

    @Schema(description = "비밀번호" ,example = "password123")
    String password;

}
