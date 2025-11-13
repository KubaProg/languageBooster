package eu.pl.main.repository;

import eu.pl.main.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    long countByCollectionId(UUID collectionId);
    List<Card> findAllByCollectionId(UUID collectionId);
}
