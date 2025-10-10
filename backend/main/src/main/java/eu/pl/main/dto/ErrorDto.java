package eu.pl.main.dto;

import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;

public record ErrorDto(
    String message,
    HttpStatus status,
    int statusCode,
    OffsetDateTime timestamp
) {}
