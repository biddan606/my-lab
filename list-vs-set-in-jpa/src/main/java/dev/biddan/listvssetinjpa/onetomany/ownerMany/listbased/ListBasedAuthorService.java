package dev.biddan.listvssetinjpa.onetomany.ownerMany.listbased;

import dev.biddan.listvssetinjpa.onetomany.ownerMany.Book;
import dev.biddan.listvssetinjpa.onetomany.ownerMany.BookRepository;
import dev.biddan.listvssetinjpa.onetomany.ownerMany.setbased.ListBasedAuthor;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListBasedAuthorService {

    private final ListBasedAuthorRepository listBasedAuthorRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void registerAuthor(Long authorId, Long bookId) {
        Book book = bookRepository.getBookById(bookId);
        ListBasedAuthor author = listBasedAuthorRepository.getAuthorById(authorId);

        author.addBook(book);
    }

    @Transactional
    public void unregisterAuthor(Long authorId, Long bookId) {
        ListBasedAuthor author = listBasedAuthorRepository.findByIdWithBooks(authorId)
                .orElseThrow(EntityNotFoundException::new);
        Book book = bookRepository.getBookById(bookId);

        author.removeBook(book);
    }
}
