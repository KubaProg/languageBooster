package eu.pl.main.exception.auth;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidSubjectFormatException extends BusinessException {

    public InvalidSubjectFormatException() {
        super(HttpStatus.UNAUTHORIZED,"JWT subject is not a UUID");
    }
}
