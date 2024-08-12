package dev.biddan.listvssetinjpa.onetomany.ownerone.listbased;

import dev.biddan.listvssetinjpa.onetomany.ownerone.Employee;
import dev.biddan.listvssetinjpa.onetomany.ownerone.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListBasedDepartmentService {

    private final ListBasedDepartmentRepository listBasedDepartmentRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public void addEmployee(Long departmentId, Employee employee) {
        ListBasedDepartment department = listBasedDepartmentRepository.getDepartmentById(departmentId);
        department.addEmployee(employee);
    }

    @Transactional
    public void removeEmployee(Long departmentId, Long employeeId) {
        ListBasedDepartment department = listBasedDepartmentRepository.findByIdWithEmployees(departmentId)
                .orElseThrow(EntityNotFoundException::new);
        Employee employee = employeeRepository.getEmployeeById(employeeId);

        department.removeEmployee(employee);
    }

    @Transactional
    public void updateEmployeeName(Long departmentId, Long employeeId, String newName) {
        ListBasedDepartment department = listBasedDepartmentRepository.findByIdWithEmployees(departmentId)
                .orElseThrow(EntityNotFoundException::new);

        department.updateEmployeeName(employeeId, newName);
    }
}
