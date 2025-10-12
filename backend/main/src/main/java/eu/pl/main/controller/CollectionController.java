package eu.pl.main.controller;

import eu.pl.main.dto.CollectionResponseDto;
import eu.pl.main.service.AuthService;
import eu.pl.main.service.CollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/collections")
@RequiredArgsConstructor
@Slf4j
public class CollectionController {

    private final CollectionService collectionService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<CollectionResponseDto>> getAllCollections() {
        UUID ownerId = authService.getAuthenticatedUserId();
        List<CollectionResponseDto> collections = collectionService.getAllCollectionsForUser(UUID.fromString("f885b81a-6541-47e1-ab5e-b8bf5d6a00a7"));
        return ResponseEntity.ok(collections);
    }
}
