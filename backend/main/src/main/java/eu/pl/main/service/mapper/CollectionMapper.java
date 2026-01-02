package eu.pl.main.service.mapper;

import eu.pl.main.dto.CardDto;
import eu.pl.main.dto.CollectionResponseDto;
import eu.pl.main.entity.Card;
import eu.pl.main.entity.Collection;

public class CollectionMapper {

    public static CollectionResponseDto mapToDto(Collection collection) {
        return new CollectionResponseDto(
                collection.getId(),
                collection.getName(),
                collection.getBaseLang(),
                collection.getTargetLang(),
                collection.getCreatedAt(),
                collection.getCards().size()
        );
    }

    public static CardDto mapToCardDto(Card card) {
        return new CardDto(
                card.getId(),
                card.getFront(),
                card.getBack(),
                card.isKnown(),
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }

}
