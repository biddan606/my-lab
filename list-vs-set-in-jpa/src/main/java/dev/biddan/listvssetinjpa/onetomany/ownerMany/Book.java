package dev.biddan.listvssetinjpa.onetomany.ownerMany;

import dev.biddan.listvssetinjpa.onetomany.ownerMany.setbased.ListBasedAuthor;
import dev.biddan.listvssetinjpa.onetomany.ownerMany.setbased.SetBasedAuthor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setbased_author_id")
    private SetBasedAuthor setBasedAuthor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listBased_author_id")
    private ListBasedAuthor listBasedAuthor;

    public Book(String title) {
        this.title = title;
    }

    public void registerSetBasedAuthor(SetBasedAuthor setBasedAuthor) {
        this.setBasedAuthor = setBasedAuthor;
    }

    public void unregisterSetBasedAuthor() {
        setBasedAuthor = null;
    }

    public void registerListBasedAuthor(ListBasedAuthor listBasedAuthor) {
        this.listBasedAuthor = listBasedAuthor;
    }

    public void unregisterListBasedAuthor() {
        listBasedAuthor = null;
    }
}
