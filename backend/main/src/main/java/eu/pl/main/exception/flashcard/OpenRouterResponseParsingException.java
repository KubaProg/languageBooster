package eu.pl.main.exception.flashcard;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OpenRouterResponseParsingException extends BusinessException {
    public OpenRouterResponseParsingException(Throwable cause) {
        super(HttpStatus.BAD_GATEWAY, "Failed to deserialize response from OpenRouter API: " + cause.getMessage());
    }
}
