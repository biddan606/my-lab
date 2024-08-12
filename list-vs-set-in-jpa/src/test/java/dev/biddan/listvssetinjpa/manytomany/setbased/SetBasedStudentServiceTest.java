package dev.biddan.listvssetinjpa.manytomany.setbased;

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
class SetBasedStudentServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(SetBasedStudentServiceTest.class);

    @Autowired
    private SetBasedStudentRepository setBasedStudentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SetBasedStudentService setBasedStudentService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    SetBasedStudent student;

    List<Course> courses;

    @BeforeEach
    void setupData() {
        // 초기데이터: 학생 1명이 5개의 수업을 듣고 있는 상태
        courses = IntStream.range(0, 5)
                .mapToObj(i -> new Course("수업" + i))
                .toList();
        courseRepository.saveAll(courses);

        student = new SetBasedStudent("학생");
        courses.forEach(student::addCourse);
        setBasedStudentRepository.save(student);
    }

    @AfterEach
    void clearData() {
        transactionTemplate.execute(status -> {
            entityManager.createNativeQuery("DELETE FROM setbased_student_course").executeUpdate();
            entityManager.flush();

            courseRepository.deleteAllInBatch();
            setBasedStudentRepository.deleteAllInBatch();
            return null;
        });
    }

    @DisplayName("학생이 수업을 수강한다.")
    @Test
    void addCourse_success() {
        // given
        Course courseToAdd = courseRepository.save(new Course("새로운 수업"));

        // when
        logger.info("수강 직전");
        /**
         * 추가 쿼리:
         * 1. 학생의 모든 코스 조회
         *
         * 추정 원인:
         * - set 중복 X
         */
        setBasedStudentService.addCourse(student.getId(), courseToAdd.getId());
        logger.info("수강 직후");

        // then
        Optional<SetBasedStudent> studentOptional = setBasedStudentRepository.findByIdWithCourses(student.getId());
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
        setBasedStudentService.updateCourseName(student.getId(), courseToChangeName.getId(), nameToChange);
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
        logger.info("수강 취소 직전");
        setBasedStudentService.removeCourseFromStudent(student.getId(), courseToRemove.getId());
        logger.info("수강 취소 직후");

        // then
        Optional<SetBasedStudent> studentOptional = setBasedStudentRepository.findByIdWithCourses(student.getId());
        assertThat(studentOptional).isPresent();
        assertThat(studentOptional.get().getCourses().stream().anyMatch(
                c -> c.getId().equals(courseToRemove.getId())
        )).isFalse();
    }
}
