package eu.pl.main.exception.card;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class CardNotFoundException extends BusinessException {

    public CardNotFoundException(UUID cardId) {
        super(HttpStatus.NOT_FOUND, "Card not found with ID: " + cardId);
    }
}
