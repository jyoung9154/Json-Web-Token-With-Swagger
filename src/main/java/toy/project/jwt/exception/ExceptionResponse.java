package toy.project.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ExceptionResponse extends RuntimeException{
    ErrorCode errorCode;

    @Setter
    String message;

    public ExceptionResponse(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
