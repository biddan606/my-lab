package dev.biddan.listvssetinjpa.onetomany.ownerone;

import dev.biddan.listvssetinjpa.onetomany.ownerone.setbased.SetBasedDepartment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    private String name;

    public Employee(String name) {
        this.name = name;
    }

    public void changeName(String newName) {
        this.name = newName;
    }
}
