package eu.pl.main.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(BusinessException exception) {
        return ResponseEntity.status(exception.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorResponse.builder()
                                .message(exception.getMessage())
                                .build()
                );
    }
    @Builder
    @Getter
    public static class ErrorResponse {
        private String message;
    }


}
