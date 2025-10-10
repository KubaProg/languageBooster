package eu.pl.main.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // This is a placeholder implementation.
        // In a real application, you would load user details from a database or other source.
        if ("testuser".equals(username)) {
            // Using a fixed UUID for demonstration purposes
            UUID dummyUserId = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef");
            return new CustomUserDetails(dummyUserId, "testuser", "password"); // Password is not used with permitAll()
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
