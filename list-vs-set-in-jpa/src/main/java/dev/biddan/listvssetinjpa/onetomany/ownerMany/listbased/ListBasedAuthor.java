package dev.biddan.listvssetinjpa.onetomany.ownerMany.setbased;

import dev.biddan.listvssetinjpa.onetomany.ownerMany.Book;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ListBasedAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "listBasedAuthor")
    private List<Book> books = new ArrayList<>();

    public ListBasedAuthor(String name) {
        this.name = name;
    }

    public void addBook(Book book) {
        books.add(book);
        book.registerListBasedAuthor(this);
    }

    public void removeBook(Book book) {
        if (books.remove(book)) {
            book.unregisterListBasedAuthor();
        }
    }
}
