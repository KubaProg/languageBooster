package eu.pl.main.exception.card;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotAuthorizedForCardException extends BusinessException {

    public UserNotAuthorizedForCardException(UUID cardId, UUID ownerId) {
        super(HttpStatus.FORBIDDEN, "User " + ownerId + " is not authorized to access card " + cardId);
    }
}
