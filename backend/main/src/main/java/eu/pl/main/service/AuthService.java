package eu.pl.main.service;

import eu.pl.main.exception.auth.InvalidSubjectFormatException;
import eu.pl.main.exception.auth.SubjectInTokenMissingException;
import eu.pl.main.exception.auth.UnauthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    public UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("No authentication in context");
            throw new UnauthenticatedException();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String sub = jwt.getSubject();
            if (sub == null || sub.isBlank()) {
                log.warn("JWT subject (sub) missing");
                throw new SubjectInTokenMissingException();
            }
            try {
                UUID userId = UUID.fromString(sub);
                log.debug("Authenticated userId: {}", userId);
                return userId;
            } catch (IllegalArgumentException ex) {
                log.warn("JWT subject is not a UUID: {}", sub);
                throw new InvalidSubjectFormatException();
            }
        }

        log.warn("Principal is not a Jwt: {}", principal != null ? principal.getClass() : "null");
        throw new UnauthenticatedException();
    }
}
