package dev.biddan.listvssetinjpa.manytomany.listbased;

import dev.biddan.listvssetinjpa.manytomany.setbased.SetBasedStudent;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ListBasedStudentRepository extends JpaRepository<ListBasedStudent, Long> {
    @Query("SELECT s FROM ListBasedStudent s LEFT JOIN FETCH s.courses WHERE s.id = :id")
    Optional<ListBasedStudent> findByIdWithCourses(Long id);

    default ListBasedStudent getStudentById(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
