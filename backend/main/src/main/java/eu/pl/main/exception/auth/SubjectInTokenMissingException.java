package eu.pl.main.exception.auth;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubjectInTokenMissingException extends BusinessException {

    public SubjectInTokenMissingException() {
        super(HttpStatus.UNAUTHORIZED,"Invalid token: subject missing");
    }
}
