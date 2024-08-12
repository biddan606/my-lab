package dev.biddan.listvssetinjpa.onetomany.ownerMany.listbased;

import dev.biddan.listvssetinjpa.onetomany.ownerMany.setbased.ListBasedAuthor;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ListBasedAuthorRepository extends JpaRepository<ListBasedAuthor, Long> {

    @Query("SELECT a FROM ListBasedAuthor a LEFT JOIN FETCH a.books WHERE a.id = :id")
    Optional<ListBasedAuthor> findByIdWithBooks(Long id);

    default ListBasedAuthor getAuthorById(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
