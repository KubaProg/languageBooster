package eu.pl.main.dto.openrouter;

import java.util.Map;

public record JsonSchema(String name, boolean strict, Map<String, Object> schema) {}