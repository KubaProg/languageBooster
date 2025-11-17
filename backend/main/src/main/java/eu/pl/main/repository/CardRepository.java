package eu.pl.main.repository;

import eu.pl.main.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    long countByCollectionId(UUID collectionId);
    List<Card> findAllByCollectionIdAndKnownIsFalse(UUID collectionId);
    @Modifying
    @Query("UPDATE Card c SET c.known = false WHERE c.collection.id = :collectionId")
    void resetKnownStatusForCollection(@Param("collectionId") UUID collectionId);
}
