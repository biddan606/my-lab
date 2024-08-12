package dev.biddan.listvssetinjpa.manytomany;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    default Course getCourseById(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
