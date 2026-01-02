package eu.pl.main.exception.flashcard;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OpenRouterEmptyContentException extends BusinessException {
    public OpenRouterEmptyContentException() {
        super(HttpStatus.BAD_GATEWAY, "OpenRouter returned empty content.");
    }
}
