package dev.biddan.listvssetinjpa.onetomany.ownerone.setbased;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SetBasedDepartmentRepository extends JpaRepository<SetBasedDepartment, Long> {

    @Query("SELECT d FROM SetBasedDepartment d LEFT JOIN FETCH d.employees WHERE d.id = :id")
    Optional<SetBasedDepartment> findByIdWithEmployees(Long id);

    default SetBasedDepartment getDepartmentById(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
