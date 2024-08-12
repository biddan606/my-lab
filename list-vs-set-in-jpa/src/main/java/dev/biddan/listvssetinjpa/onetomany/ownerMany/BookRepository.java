package dev.biddan.listvssetinjpa.onetomany.ownerMany;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    default Book getBookById(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
