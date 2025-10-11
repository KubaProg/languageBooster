package eu.pl.main.dto.openrouter;

public record OpenRouterUsage(int prompt_tokens, int completion_tokens, int total_tokens) {}