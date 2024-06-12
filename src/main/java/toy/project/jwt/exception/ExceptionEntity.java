package toy.project.jwt.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ExceptionEntity {
    private int status;
    private String name;
    private String code;
    private String errorMessage;
    private String note;

    public static ResponseEntity<ExceptionEntity> toResponseEntity(ErrorCode e, String note) {
        if(note == null){
            note = "";
        }
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ExceptionEntity.builder()
                        .status(e.getHttpStatus().value())
                        .name(e.name())
                        .code(e.getCode())
                        .errorMessage(e.getMessage())
                        .note(note)
                        .build());
    }
}
