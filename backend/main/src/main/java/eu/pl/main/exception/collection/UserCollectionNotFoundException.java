package eu.pl.main.exception.collection;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserCollectionNotFoundException extends BusinessException {

    public UserCollectionNotFoundException(UUID collectionId, UUID ownerId) {
        super(HttpStatus.NOT_FOUND,"Collection not found with ID: " + collectionId + " for user: " + ownerId );
    }
}
