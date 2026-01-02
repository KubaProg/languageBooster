package eu.pl.main.exception.flashcard;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OpenRouterEmptyResponseException extends BusinessException {
    public OpenRouterEmptyResponseException() {
        super(HttpStatus.BAD_GATEWAY, "Empty response from OpenRouter API.");
    }
}
