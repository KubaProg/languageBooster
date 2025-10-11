package eu.pl.main.exception;

import eu.pl.main.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletionException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorDto> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("Error from external API: Status {}, Body {}", ex.getStatusCode(), ex.getResponseBodyAsString());
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        ErrorDto errorDto = new ErrorDto(
                "Error communicating with the generation service.",
                status,
                status.value(),
                OffsetDateTime.now()
        );
        return new ResponseEntity<>(errorDto, status);
    }

    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<ErrorDto> handleCompletionException(CompletionException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "An unexpected error occurred during an asynchronous operation";

        if (ex.getCause() instanceof com.fasterxml.jackson.core.JsonProcessingException) {
            log.error("Failed to process JSON response from external API", ex.getCause());
            message = "Invalid response format from the generation service.";
        } else {
            log.error(message, ex);
        }

        ErrorDto errorDto = new ErrorDto(
                message,
                status,
                status.value(),
                OffsetDateTime.now()
        );
        return new ResponseEntity<>(errorDto, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
        log.error("An unexpected error occurred", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDto errorDto = new ErrorDto(
                "An internal server error occurred.",
                status,
                status.value(),
                OffsetDateTime.now()
        );
        return new ResponseEntity<>(errorDto, status);
    }
}