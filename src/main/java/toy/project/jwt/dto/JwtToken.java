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
@Schema(title = "JWT 토큰")
public class JwtToken {

    @Schema(description = "접근 토큰")
    String accessToken;

    @Schema(description = "갱신 토큰")
    String refreshToken;

}
