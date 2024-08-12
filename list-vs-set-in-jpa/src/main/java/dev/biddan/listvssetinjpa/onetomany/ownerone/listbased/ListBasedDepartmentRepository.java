package dev.biddan.listvssetinjpa.onetomany.ownerone.listbased;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ListBasedDepartmentRepository extends JpaRepository<ListBasedDepartment, Long> {

    @Query("SELECT d FROM ListBasedDepartment d LEFT JOIN FETCH d.employees WHERE d.id = :id")
    Optional<ListBasedDepartment> findByIdWithEmployees(Long id);

    default ListBasedDepartment getDepartmentById(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
