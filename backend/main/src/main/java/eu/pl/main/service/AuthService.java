package eu.pl.main.service;

import eu.pl.main.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    public UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID ownerId = null;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            ownerId = customUserDetails.getId();
            log.info("Authenticated user ID: {}", ownerId);
        } else {
            // Fallback for testing or if authentication is not fully set up
            ownerId = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef"); 
            log.warn("Could not retrieve authenticated user ID from security context. Using fallback ID: {}", ownerId);
        }
        return ownerId;
    }
}
