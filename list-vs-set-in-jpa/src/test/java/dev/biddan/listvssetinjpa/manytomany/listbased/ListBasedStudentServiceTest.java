package dev.biddan.listvssetinjpa.manytomany.listbased;

import static org.assertj.core.api.Assertions.assertThat;

import dev.biddan.listvssetinjpa.manytomany.Course;
import dev.biddan.listvssetinjpa.manytomany.CourseRepository;
import jakarta.persistence.EntityManager;
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
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class ListBasedStudentServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ListBasedStudentService.class);

    @Autowired
    private ListBasedStudentRepository listBasedStudentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ListBasedStudentService listBasedStudentService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private ListBasedStudent student;

    private List<Course> courses;

    @BeforeEach
    void setupData() {
        // 초기데이터: 학생 1명이 5개의 수업을 듣고 있는 상태
        courses = IntStream.range(0, 5)
                .mapToObj(i -> new Course("수업" + i))
                .toList();
        courseRepository.saveAll(courses);

        student = new ListBasedStudent("학생");
        courses.forEach(student::addCourse);
        listBasedStudentRepository.save(student);
    }

    @AfterEach
    void clearData() {
        transactionTemplate.execute(status -> {
            entityManager.createNativeQuery("DELETE FROM listbased_student_course").executeUpdate();
            entityManager.flush();

            courseRepository.deleteAllInBatch();
            listBasedStudentRepository.deleteAllInBatch();
            return null;
        });
    }

    @DisplayName("학생이 수업을 수강한다.")
    @Test
    void addCourse_success() {
        // given
        Course courseToAdd = courseRepository.save(new Course("새로운 수업"));

        // when
        /**
         * 추가 쿼리:
         * 1. 학생의 모든 코스 조회
         * 2. 학생의 모든 코스 관계 제거
         * 3. 모든 코스 재삽입
         *
         * 주의:
         * - @ManyToMany + List 조합에서만 발생
         * - @OrderColumn 설정 시 새로운 코스만 삽입됨
         *
         * 추정 원인:
         * 1. 중간 엔티티 부재로 변경 추적 어려움
         * 2. List 사용으로 순서 중요성 암시, 그러나 @OrderColumn 없어 순서 보장 불가
         * 3. 데이터 일관성을 위해 전체 삭제 후 재삽입 방식 선택
         *
         * 결과:
         * - 데이터 일관성 보장
         * - 성능 저하 가능성 존재
         */
        logger.info("수강 직전");
        listBasedStudentService.addCourse(student.getId(), courseToAdd.getId());
        logger.info("수강 직후");

        // then
        Optional<ListBasedStudent> studentOptional = listBasedStudentRepository.findByIdWithCourses(student.getId());
        assertThat(studentOptional).isPresent();
        assertThat(studentOptional.get().getCourses()).size().isEqualTo(6);
    }

    @DisplayName("학생이 듣고 있는 수업 정보를 변경한다.")
    @Test
    void updateCourseName() {
        // given
        Course courseToChangeName = courses.getFirst();
        String nameToChange = "새로운 이름";

        // when
        logger.info("변경 직전");
        listBasedStudentService.updateCourseName(student.getId(), courseToChangeName.getId(), nameToChange);
        logger.info("변경 직후");

        // then
        Optional<Course> courseOptional = courseRepository.findById(courseToChangeName.getId());
        assertThat(courseOptional).isPresent();
        assertThat(courseOptional.get().getName()).isEqualTo(nameToChange);
    }

    @DisplayName("학생이 수강 중인 수업을 취소한다.")
    @Test
    void removeCourse() {
        // given
        Course courseToRemove = courses.getFirst();

        // when
        // addCourse와 동일
        logger.info("수강 취소 직전");
        listBasedStudentService.removeCourseFromStudent(student.getId(), courseToRemove.getId());
        logger.info("수강 취소 직후");

        // then
        Optional<ListBasedStudent> studentOptional = listBasedStudentRepository.findByIdWithCourses(student.getId());
        assertThat(studentOptional).isPresent();
        assertThat(studentOptional.get().getCourses().stream().anyMatch(
                c -> c.getId().equals(courseToRemove.getId())
        )).isFalse();
    }
}
