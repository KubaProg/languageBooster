package eu.pl.main.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    private static final UUID MOCK_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    public UUID getAuthenticatedUserId() {
        log.debug("Using mock authenticated userId: {}", MOCK_USER_ID);
        return MOCK_USER_ID;
    }
}
