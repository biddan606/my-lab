package dev.biddan.listvssetinjpa.onetomany.ownerMany.setbased;

import static org.assertj.core.api.Assertions.assertThat;

import dev.biddan.listvssetinjpa.onetomany.ownerMany.Book;
import dev.biddan.listvssetinjpa.onetomany.ownerMany.BookRepository;
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
class SetBasedAuthorServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(SetBasedAuthorServiceTest.class);

    @Autowired
    private SetBasedAuthorService setBasedAuthorService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SetBasedAuthorRepository setBasedAuthorRepository;


    private List<Book> books;

    private SetBasedAuthor author;

    @BeforeEach
    void setupData() {
        // 초기데이터: 저자 1명이 5개의 책을 가지고 있는 상태
        author = new SetBasedAuthor("저자");
        setBasedAuthorRepository.save(author);

        books = IntStream.range(0, 5)
                .mapToObj(i -> new Book("책 이름" + i))
                .toList();

        books.forEach(author::addBook);
        bookRepository.saveAll(books);
    }

    @AfterEach
    void clearData() {
        bookRepository.deleteAllInBatch();
        setBasedAuthorRepository.deleteAllInBatch();
    }

    @DisplayName("책에 저자를 등록한다.")
    @Test
    void addBook() {
        // given
        Book book = new Book("새로운 책");
        bookRepository.save(book);

        // when
        logger.info("등록 직전");
        /**
         * 추가 쿼리:
         * - author.addBook할 때, books를 가져옴,
         *
         * 이유 추측:
         * - Set은 중복 제거 -> 기존 데이터 중 중복이 있는지 비교 -> 컬렉션 조회
         */
        setBasedAuthorService.registerAuthor(author.getId(), book.getId());
        logger.info("등록 직후");

        // then
        Optional<SetBasedAuthor> authorOptional = setBasedAuthorRepository.findByIdWithBooks(author.getId());
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
        setBasedAuthorService.unregisterAuthor(author.getId(), book.getId());
        logger.info("해지 직후");

        // then
        Optional<SetBasedAuthor> authorOptional = setBasedAuthorRepository.findByIdWithBooks(author.getId());
        assertThat(authorOptional).isPresent();
        assertThat(authorOptional.get().getBooks()).size().isEqualTo(4);
        assertThat(authorOptional.get()
                .getBooks().stream()
                .anyMatch(b -> b.getId().equals(book.getId()))
        ).isFalse();
    }
}
