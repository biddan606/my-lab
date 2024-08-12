package dev.biddan.listvssetinjpa.onetomany.ownerone.listbased;

import dev.biddan.listvssetinjpa.onetomany.ownerone.Employee;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ListBasedDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "listbased_department_id")
    private List<Employee> employees = new ArrayList<>();

    public ListBasedDepartment(String name) {
        this.name = name;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    public void updateEmployeeName(Long employeeId, String newName) {
        Employee employee = employees.stream()
                .filter(e -> e.getId().equals(employeeId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        employee.changeName(newName);
    }
}
