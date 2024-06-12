package toy.project.jwt.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* 커스텀 예외 처리기 */
    @ExceptionHandler(ExceptionResponse.class)
    protected ResponseEntity<ExceptionEntity> handleCustomException(ExceptionResponse e){
        return ExceptionEntity.toResponseEntity(e.getErrorCode(),e.getMessage());
    }
}

