package eu.pl.main.dto.openrouter;

import java.util.List;

public record OpenRouterChatResponse(String id, List<OpenRouterChoice> choices, OpenRouterUsage usage) {}