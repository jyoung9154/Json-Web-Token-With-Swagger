package toy.project.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 사용자 */
    USER_NOT_FONUD(HttpStatus.NOT_FOUND, "USER_001", "사용자 정보를 다시 확인해주세요."),
    USER_DUPLICATION_ID(HttpStatus.CONFLICT, "USER_002", "해당 아이디가 이미 존재합니다."),
    USER_DUPLICATION_REG(HttpStatus.CONFLICT, "USER_003", "해당 사용자가 이미 존재합니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER_004", "비밀번호가 일치하지 않습니다."),
    USER_REG_ERROR(HttpStatus.BAD_REQUEST, "USER_005", "주민등록번호가 잘못 입력되었습니다."),
    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "USER_006", "허가된 사용자가 아닙니다."),

    /* JWT 토큰 */
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT_001", "토큰이 만료되었습니다."),
    JWT_MALFORMED(HttpStatus.UNAUTHORIZED, "JWT_002", "잘못된 토큰입니다."),
    JWT_ILLEGAL(HttpStatus.UNAUTHORIZED, "JWT_003", "토큰을 입력해주세요."),
    JWT_REFRESH(HttpStatus.UNAUTHORIZED, "JWT_004", "소유하신 토큰에 접근 권한이 없습니다."),
    JWT_UNAUTH(HttpStatus.UNAUTHORIZED, "JWT_005", "토큰을 다시 확인해주세요."),

    /* DB */
    DB_FAILD_SAVE(HttpStatus.INTERNAL_SERVER_ERROR,"DB_001", "DB 저장 중 오류가 발생하였습니다."),

    /* 암호화 */
    AES_ENCODER(HttpStatus.INTERNAL_SERVER_ERROR,"AES_001", "AES 암호화 중 에러가 발생하였습니다."),
    AES_DECODER(HttpStatus.INTERNAL_SERVER_ERROR,"AES_002", "AES 복호화 중 에러가 발생하였습니다."),

    /* 서버 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"SER_001", "서버에서 에러가 발생하였습니다.");

    /* Http 상태값 */
    private final HttpStatus httpStatus;

    /* 에러코드 */
    private final String code;

    /* 에러 메세지 */
    private final String message;

}