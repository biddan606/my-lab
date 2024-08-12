package dev.biddan.listvssetinjpa.onetomany.ownerMany.setbased;

import dev.biddan.listvssetinjpa.onetomany.ownerMany.Book;
import dev.biddan.listvssetinjpa.onetomany.ownerMany.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SetBasedAuthorService {

    private final SetBasedAuthorRepository setBasedAuthorRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void registerAuthor(Long authorId, Long bookId) {
        Book book = bookRepository.getBookById(bookId);
        SetBasedAuthor author = setBasedAuthorRepository.getAuthorById(authorId);

        author.addBook(book);
    }

    @Transactional
    public void unregisterAuthor(Long authorId, Long bookId) {
        SetBasedAuthor author = setBasedAuthorRepository.findByIdWithBooks(authorId)
                .orElseThrow(EntityNotFoundException::new);
        Book book = bookRepository.getBookById(bookId);

        author.removeBook(book);
    }
}
