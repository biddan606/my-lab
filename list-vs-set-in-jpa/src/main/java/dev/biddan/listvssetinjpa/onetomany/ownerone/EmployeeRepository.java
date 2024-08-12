package dev.biddan.listvssetinjpa.onetomany.ownerone;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    default Employee getEmployeeById(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
