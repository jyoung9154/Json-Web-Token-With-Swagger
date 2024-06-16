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
@Schema( title = "회원가입 요청 DTO")
public class SignupRequest {

    @Schema(description = "사용자 아이디", example = "jyoung9154")
    private String userId;

    @Schema(description = "비밀번호", example = "password123")
    private String password;

    @Schema(description = "이름", example = "조조")
    private String name;

    @Schema(description = "주민등록번호", example = "810326-2715702")
    private String regNo;
}
