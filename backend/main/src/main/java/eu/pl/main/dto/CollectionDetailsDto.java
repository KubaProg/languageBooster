package eu.pl.main.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record CollectionDetailsDto(
        UUID id,
        String name,
        String baseLang,
        String targetLang,
        OffsetDateTime createdAt,
        List<CardDto> cards
) {}
