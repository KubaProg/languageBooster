package eu.pl.main.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CollectionResponseDto(
    UUID id,
    String name,
    String baseLang,
    String targetLang,
    OffsetDateTime createdAt,
    long cardCount
) {}
