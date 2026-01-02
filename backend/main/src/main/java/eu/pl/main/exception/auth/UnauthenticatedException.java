package eu.pl.main.exception.auth;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UnauthenticatedException extends BusinessException {

    public UnauthenticatedException() {
        super(HttpStatus.UNAUTHORIZED,"You need to authenticate first");
    }
}
