package eu.pl.main.service;

import eu.pl.main.dto.CollectionResponseDto;
import eu.pl.main.entity.Collection;
import eu.pl.main.repository.CardRepository;
import eu.pl.main.repository.CollectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final CardRepository cardRepository;

    @Transactional(readOnly = true)
    public List<CollectionResponseDto> getAllCollectionsForUser(UUID ownerId) {
        log.info("Fetching collections for ownerId: {}", ownerId);
        return collectionRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCollection(UUID collectionId, UUID ownerId) {
        log.info("Attempting to delete collection with ID: {} for owner: {}", collectionId, ownerId);

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collection not found with ID: " + collectionId));

        if (!collection.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException("User is not authorized to delete this collection.");
        }

        collectionRepository.deleteById(collectionId);
        log.info("Collection with ID: {} successfully deleted for owner: {}", collectionId, ownerId);
    }

    private CollectionResponseDto mapToDto(Collection collection) {
        long cardCount = cardRepository.countByCollectionId(collection.getId());
        return new CollectionResponseDto(
                collection.getId(),
                collection.getName(),
                collection.getBaseLang(),
                collection.getTargetLang(),
                collection.getCreatedAt(),
                cardCount
        );
    }
}