package dev.biddan.listvssetinjpa.manytomany;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    private String name;

    public Course(String name) {
        this.name = name;
    }

    public void changeName(String newName) {
        this.name = newName;
    }
}
