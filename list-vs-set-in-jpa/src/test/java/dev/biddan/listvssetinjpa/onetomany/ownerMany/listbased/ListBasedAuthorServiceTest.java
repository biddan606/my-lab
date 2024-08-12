package dev.biddan.listvssetinjpa.onetomany.ownerMany.listbased;

import static org.assertj.core.api.Assertions.assertThat;

import dev.biddan.listvssetinjpa.onetomany.ownerMany.Book;
import dev.biddan.listvssetinjpa.onetomany.ownerMany.BookRepository;
import dev.biddan.listvssetinjpa.onetomany.ownerMany.setbased.ListBasedAuthor;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ListBasedAuthorServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ListBasedAuthorServiceTest.class);

    @Autowired
    private ListBasedAuthorService listBasedAuthorService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ListBasedAuthorRepository listBasedAuthorRepository;


    private List<Book> books;

    private ListBasedAuthor author;

    @BeforeEach
    void setupData() {
        // 초기데이터: 저자 1명이 5개의 책을 가지고 있는 상태
        author = new ListBasedAuthor("저자");
        listBasedAuthorRepository.save(author);

        books = IntStream.range(0, 5)
                .mapToObj(i -> new Book("책 이름" + i))
                .toList();

        books.forEach(author::addBook);
        bookRepository.saveAll(books);
    }

    @AfterEach
    void clearData() {
        bookRepository.deleteAllInBatch();
        listBasedAuthorRepository.deleteAllInBatch();
    }

    @DisplayName("책에 저자를 등록한다.")
    @Test
    void addBook() {
        // given
        Book book = new Book("새로운 책");
        bookRepository.save(book);

        // when
        logger.info("등록 직전");
        // author.addBook할 때, books를 불러오지 않음(중복 체크할 필요 없고 mappedby는 기본적으로 읽기 전용이기 때문에)
        listBasedAuthorService.registerAuthor(author.getId(), book.getId());
        logger.info("등록 직후");

        // then
        Optional<ListBasedAuthor> authorOptional = listBasedAuthorRepository.findByIdWithBooks(author.getId());
        assertThat(authorOptional).isPresent();
        assertThat(authorOptional.get()
                .getBooks().stream()
                .anyMatch(b -> b.getId().equals(book.getId()))
        ).isTrue();
    }

    @DisplayName("책의 저자를 해지한다.")
    @Test
    void removeBook() {
        // given
        Book book = books.getFirst();

        // when
        logger.info("해지 직전");
        listBasedAuthorService.unregisterAuthor(author.getId(), book.getId());
        logger.info("해지 직후");

        // then
        Optional<ListBasedAuthor> authorOptional = listBasedAuthorRepository.findByIdWithBooks(author.getId());
        assertThat(authorOptional).isPresent();
        assertThat(authorOptional.get().getBooks()).size().isEqualTo(4);
        assertThat(authorOptional.get()
                .getBooks().stream()
                .anyMatch(b -> b.getId().equals(book.getId()))
        ).isFalse();
    }
}
