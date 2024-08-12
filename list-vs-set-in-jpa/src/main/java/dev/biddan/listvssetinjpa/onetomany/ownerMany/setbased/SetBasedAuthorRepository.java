package dev.biddan.listvssetinjpa.onetomany.ownerMany.setbased;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SetBasedAuthorRepository extends JpaRepository<SetBasedAuthor, Long> {

    @Query("SELECT a FROM SetBasedAuthor a LEFT JOIN FETCH a.books WHERE a.id = :id")
    Optional<SetBasedAuthor> findByIdWithBooks(Long id);

    default SetBasedAuthor getAuthorById(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
