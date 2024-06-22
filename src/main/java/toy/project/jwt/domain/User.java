package toy.project.jwt.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
@Schema(title = "유저 정보")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "테이블 시퀀스", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "사용자 아이디", example = "jyoung9154")
    private String userId;

    @Column(nullable = false)
    @Schema(description = "비밀번호", example = "password123")
    private String password;

    @Column(nullable = false)
    @Schema(description = "이름", example = "재영")
    private String name;

    @Column(nullable = false)
    @Schema(description = "주민등록번호", example = "920709-1000000")
    private String regNo;

}
