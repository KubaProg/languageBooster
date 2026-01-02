package eu.pl.main.service;

import eu.pl.main.dto.CardDto;
import eu.pl.main.dto.CollectionDetailsDto;
import eu.pl.main.dto.CollectionResponseDto;
import eu.pl.main.entity.Card;
import eu.pl.main.entity.Collection;
import eu.pl.main.exception.collection.UserCollectionNotFoundException;
import eu.pl.main.repository.CardRepository;
import eu.pl.main.repository.CollectionRepository;
import eu.pl.main.service.mapper.CollectionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .map(CollectionMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CollectionDetailsDto getCollectionDetails(UUID collectionId, UUID ownerId) {
        log.info("Fetching collection details for ID: {} for owner: {}", collectionId, ownerId);

        Collection collection = collectionRepository.findByIdAndOwnerId(collectionId, ownerId).orElseThrow(
                () -> new UserCollectionNotFoundException(collectionId, ownerId)
        );

        List<Card> cards = cardRepository.findAllByCollectionIdAndKnownIsFalse(collectionId);
        List<CardDto> cardDtos = cards.stream()
                .map(CollectionMapper::mapToCardDto)
                .collect(Collectors.toList());

        return new CollectionDetailsDto(
                collection.getId(),
                collection.getName(),
                collection.getBaseLang(),
                collection.getTargetLang(),
                collection.getCreatedAt(),
                cardDtos
        );
    }

    @Transactional
    public void deleteCollection(UUID collectionId, UUID ownerId) {
        log.info("Attempting to delete collection with ID: {} for owner: {}", collectionId, ownerId);

        collectionRepository.findByIdAndOwnerId(collectionId, ownerId).orElseThrow(
                () -> new UserCollectionNotFoundException(collectionId, ownerId)
        );

        collectionRepository.deleteById(collectionId);
        log.info("Collection with ID: {} successfully deleted for owner: {}", collectionId, ownerId);
    }

    @Transactional
    public void resetCollection(UUID collectionId, UUID ownerId) {
        log.info("Attempting to reset collection with ID: {} for owner: {}", collectionId, ownerId);

        collectionRepository.findByIdAndOwnerId(collectionId, ownerId).orElseThrow(
                () -> new UserCollectionNotFoundException(collectionId, ownerId)
        );

        cardRepository.resetKnownStatusForCollection(collectionId);
        log.info("Collection with ID: {} successfully reset for owner: {}", collectionId, ownerId);
    }


}