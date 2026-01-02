package eu.pl.main.exception.flashcard;

import eu.pl.main.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UnsupportedFileTypeException extends BusinessException {
    public UnsupportedFileTypeException(String fileExtension) {
        super(HttpStatus.BAD_REQUEST, "Unsupported file type: " + fileExtension);
    }
}
