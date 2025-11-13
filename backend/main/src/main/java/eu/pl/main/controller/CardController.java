package eu.pl.main.controller;

import eu.pl.main.service.AuthService;
import eu.pl.main.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Slf4j
public class CardController {

    private final CardService cardService;
    private final AuthService authService;

    public record UpdateCardKnownStatusRequest(boolean known) {}
    public record UpdateCardContentRequest(String front, String back) {}

    @PatchMapping("/{cardId}")
    public ResponseEntity<Void> updateCardKnownStatus(@PathVariable UUID cardId, @RequestBody UpdateCardKnownStatusRequest request) {
        UUID ownerId = authService.getAuthenticatedUserId();
        cardService.updateCardKnownStatus(cardId, request.known(), ownerId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<Void> updateCardContent(@PathVariable UUID cardId, @RequestBody UpdateCardContentRequest request) {
        UUID ownerId = authService.getAuthenticatedUserId();
        cardService.updateCardContent(cardId, request.front(), request.back(), ownerId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID cardId) {
        UUID ownerId = authService.getAuthenticatedUserId();
        cardService.deleteCard(cardId, ownerId);
        return ResponseEntity.noContent().build();
    }
}
