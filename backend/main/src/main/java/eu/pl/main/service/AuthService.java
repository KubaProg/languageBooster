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
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String userId = jwt.getSubject();
            log.info("Authenticated user ID from JWT: {}", userId);
            return UUID.fromString(userId);
        }
        log.warn("Could not retrieve authenticated user ID from security context.");
        // In a real-world scenario, you might throw an exception here
        // if the user ID is essential for the operation.
        return null;
    }
}
