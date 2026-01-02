package eu.pl.main.repository;

import eu.pl.main.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, UUID> {
    List<Collection> findByOwnerId(UUID ownerId);

    Optional<Collection> findByIdAndOwnerId(UUID id, UUID ownerId);

}
