package eu.pl.main.service;

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
            throw new IllegalStateException("Unauthenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String sub = jwt.getSubject();
            if (sub == null || sub.isBlank()) {
                log.warn("JWT subject (sub) missing");
                throw new IllegalStateException("Invalid token: subject missing");
            }
            try {
                UUID userId = UUID.fromString(sub);
                log.debug("Authenticated userId: {}", userId);
                return userId;
            } catch (IllegalArgumentException ex) {
                log.warn("JWT subject is not a UUID: {}", sub);
                throw new IllegalStateException("Invalid token: subject format");
            }
        }

        log.warn("Principal is not a Jwt: {}", principal != null ? principal.getClass() : "null");
        throw new IllegalStateException("Unauthenticated");
    }
}
