package eu.pl.main.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CardDto(
        UUID id,
        String front,
        String back,
        boolean known,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
