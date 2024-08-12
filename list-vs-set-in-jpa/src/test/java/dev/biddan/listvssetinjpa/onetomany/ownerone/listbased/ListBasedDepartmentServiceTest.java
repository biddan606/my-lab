package dev.biddan.listvssetinjpa.onetomany.ownerone.listbased;

import static org.assertj.core.api.Assertions.assertThat;

import dev.biddan.listvssetinjpa.onetomany.ownerone.Employee;
import dev.biddan.listvssetinjpa.onetomany.ownerone.EmployeeRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ListBasedDepartmentServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ListBasedDepartmentServiceTest.class);

    @Autowired
    private ListBasedDepartmentService listBasedDepartmentService;

    @Autowired
    private ListBasedDepartmentRepository listBasedDepartmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private List<Employee> employees;

    private ListBasedDepartment department;

    @BeforeEach
    void setupData() {
        // 초기데이터: 1개의 부서에 5명의 직원 속한 상태
        employees = IntStream.range(0, 5)
                .mapToObj(i -> new Employee("직원" + i))
                .toList();

        department = new ListBasedDepartment("부서");
        employees.forEach(department::addEmployee);
        listBasedDepartmentRepository.save(department);
    }

    @AfterEach
    void clearData() {
        listBasedDepartmentRepository.deleteAll();
    }

    @DisplayName("부서에 직원을 추가한다.")
    @Test
    void addEmployee() {
        // given
        Employee employee = new Employee("새로운 직원");

        // when
        logger.info("직원 추가 직전");
        // set과 동일
        listBasedDepartmentService.addEmployee(department.getId(), employee);
        logger.info("직원 추가 직후");

        // then
        Optional<ListBasedDepartment> departmentOptional = listBasedDepartmentRepository.findByIdWithEmployees(
                department.getId());
        assertThat(departmentOptional).isPresent();
        ListBasedDepartment actualDepartment = departmentOptional.get();

        assertThat(actualDepartment.getEmployees().stream().anyMatch(
                e -> e.getId().equals(employee.getId()))
        ).isTrue();
    }

    @DisplayName("부서에서 직원을 제거한다.")
    @Test
    void removeEmployee() {
        // given
        Employee employee = employees.getFirst();

        // when
        // set과 동일
        logger.info("직원 제거 직전");
        listBasedDepartmentService.removeEmployee(department.getId(), employee.getId());
        logger.info("직원 제거 직후");

        // then
        Optional<ListBasedDepartment> departmentOptional = listBasedDepartmentRepository.findByIdWithEmployees(
                department.getId());
        assertThat(departmentOptional).isPresent();
        ListBasedDepartment actualDepartment = departmentOptional.get();

        assertThat(actualDepartment.getEmployees().stream().anyMatch(
                e -> e.getId().equals(employee.getId()))
        ).isFalse();
    }

    @DisplayName("부서에 속한 직원의 이름을 변경한다.")
    @Test
    void updateEmployeeName() {
        // given
        Employee employee = employees.getFirst();
        String newName = "새로운 닉네임";

        // when
        logger.info("이름 변경 직전");
        listBasedDepartmentService.updateEmployeeName(department.getId(), employee.getId(), newName);
        logger.info("이름 변경 직후");

        // then
        Employee actualEmployee = employeeRepository.getEmployeeById(employee.getId());
        assertThat(actualEmployee.getName()).isEqualTo(newName);
    }
}
