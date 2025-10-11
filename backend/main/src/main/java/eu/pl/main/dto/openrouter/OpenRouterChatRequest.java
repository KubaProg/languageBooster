package eu.pl.main.dto.openrouter;

import java.util.List;

public record OpenRouterChatRequest(
    String model,
    List<Message> messages,
    ResponseFormat response_format,
    Double temperature,
    Integer max_tokens
) {}