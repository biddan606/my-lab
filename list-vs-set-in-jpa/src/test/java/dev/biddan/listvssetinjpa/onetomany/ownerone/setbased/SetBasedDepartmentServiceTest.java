package dev.biddan.listvssetinjpa.onetomany.ownerone.setbased;

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
class SetBasedDepartmentServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(SetBasedDepartmentServiceTest.class);

    @Autowired
    private SetBasedDepartmentService setBasedDepartmentService;

    @Autowired
    private SetBasedDepartmentRepository setBasedDepartmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private List<Employee> employees;

    private SetBasedDepartment department;

    @BeforeEach
    void setupData() {
        // 초기데이터: 1개의 부서에 5명의 직원 속한 상태
        employees = IntStream.range(0, 5)
                .mapToObj(i -> new Employee("직원" + i))
                .toList();

        department = new SetBasedDepartment("부서");
        employees.forEach(department::addEmployee);
        setBasedDepartmentRepository.save(department);
    }

    @AfterEach
    void clearData() {
        setBasedDepartmentRepository.deleteAll();
    }

    @DisplayName("부서에 직원을 추가한다.")
    @Test
    void addEmployee() {
        // given
        Employee employee = new Employee("새로운 직원");

        // when
        logger.info("직원 추가 직전");
        /**
         * 추가 쿼리:
         * 1. 부서에 속한 직원
         * 2. 직원 추가
         * 3. 직원-부서 매핑
         *
         * 추정 원인:
         * - 코드와 DB 불일치
         * - 코드는 부서가 직원 키 소유, DB는 직원이 부서 키 소유
         * - 직원 추가와 매핑이 별개로 이루어짐
         */
        setBasedDepartmentService.addEmployee(department.getId(), employee);
        logger.info("직원 추가 직후");

        // then
        Optional<SetBasedDepartment> departmentOptional = setBasedDepartmentRepository.findByIdWithEmployees(
                department.getId());
        assertThat(departmentOptional).isPresent();
        SetBasedDepartment actualDepartment = departmentOptional.get();

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
        /**
         * 추카 쿼리:
         * 1. 직원-매핑 끊기
         * 2. 직원 제거
         *
         * 추정 원인:
         * - 코드와 DB 키 소유 불일치
         */
        logger.info("직원 제거 직전");
        setBasedDepartmentService.removeEmployee(department.getId(), employee.getId());
        logger.info("직원 제거 직후");

        // then
        Optional<SetBasedDepartment> departmentOptional = setBasedDepartmentRepository.findByIdWithEmployees(
                department.getId());
        assertThat(departmentOptional).isPresent();
        SetBasedDepartment actualDepartment = departmentOptional.get();

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
        setBasedDepartmentService.updateEmployeeName(department.getId(), employee.getId(), newName);
        logger.info("이름 변경 직후");

        // then
        Employee actualEmployee = employeeRepository.getEmployeeById(employee.getId());
        assertThat(actualEmployee.getName()).isEqualTo(newName);
    }
}
