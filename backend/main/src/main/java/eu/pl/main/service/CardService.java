package eu.pl.main.service;

import eu.pl.main.entity.Card;
import eu.pl.main.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private final AuthService authService;

    @Transactional
    public void updateCardKnownStatus(UUID cardId, boolean known, UUID ownerId) {
        log.info("Attempting to update known status for card ID: {} to {} for owner: {}", cardId, known, ownerId);

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found with ID: " + cardId));

        // Check if the owner of the card's collection matches the authenticated user
        if (!card.getCollection().getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException("User is not authorized to update this card.");
        }

        card.setKnown(known);
        card.setUpdatedAt(OffsetDateTime.now());
        cardRepository.save(card);
        log.info("Card ID: {} known status updated to {} for owner: {}", cardId, known, ownerId);
    }

    @Transactional
    public void updateCardContent(UUID cardId, String front, String back, UUID ownerId) {
        log.info("Attempting to update content for card ID: {} for owner: {}", cardId, ownerId);

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found with ID: " + cardId));

        if (!card.getCollection().getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException("User is not authorized to update this card.");
        }

        card.setFront(front);
        card.setBack(back);
        card.setUpdatedAt(OffsetDateTime.now());
        cardRepository.save(card);
        log.info("Card ID: {} content updated for owner: {}", cardId, ownerId);
    }

    @Transactional
    public void deleteCard(UUID cardId, UUID ownerId) {
        log.info("Attempting to delete card ID: {} for owner: {}", cardId, ownerId);

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found with ID: " + cardId));

        if (!card.getCollection().getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException("User is not authorized to delete this card.");
        }

        cardRepository.delete(card);
        log.info("Card ID: {} deleted for owner: {}", cardId, ownerId);
    }
}
