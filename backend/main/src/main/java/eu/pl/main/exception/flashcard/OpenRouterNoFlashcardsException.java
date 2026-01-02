package eu.pl.main.exception.flashcard;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OpenRouterNoFlashcardsException extends BusinessException {
    public OpenRouterNoFlashcardsException() {
        super(HttpStatus.BAD_GATEWAY, "OpenRouter did not return any flashcards.");
    }
}
