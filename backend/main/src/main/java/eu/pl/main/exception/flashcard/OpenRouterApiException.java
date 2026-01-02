package eu.pl.main.exception.flashcard;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OpenRouterApiException extends BusinessException {
    public OpenRouterApiException(Throwable cause) {
        super(HttpStatus.BAD_GATEWAY, "Error while calling OpenRouter API: " + cause.getMessage());
    }
}
