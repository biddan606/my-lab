package dev.biddan.listvssetinjpa.manytomany.setbased;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SetBasedStudentRepository extends JpaRepository<SetBasedStudent, Long> {

    @Query("SELECT s FROM SetBasedStudent s LEFT JOIN FETCH s.courses WHERE s.id = :id")
    Optional<SetBasedStudent> findByIdWithCourses(Long id);

    default SetBasedStudent getStudentById(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
