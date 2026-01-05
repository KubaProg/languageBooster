package eu.pl.main.exception.card;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProblematicFileException extends BusinessException {

    public ProblematicFileException() {
        super(HttpStatus.BAD_REQUEST, "File could not be processed, make sure file is of PDF format.");
    }
}

