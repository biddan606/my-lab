package dev.biddan.listvssetinjpa.manytomany.listbased;

import dev.biddan.listvssetinjpa.manytomany.Course;
import dev.biddan.listvssetinjpa.manytomany.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListBasedStudentService {

    private final ListBasedStudentRepository listBasedStudentRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public void addCourse(Long studentId, Long courseId) {
        Course course = courseRepository.getCourseById(courseId);
        ListBasedStudent student = listBasedStudentRepository.getStudentById(studentId);

        student.addCourse(course);
    }

    // 사용하면 안되는 메서드지만, JPA 동작을 위해 만듬
    @Transactional
    public void updateCourseName(Long studentId, Long courseId, String newName) {
        ListBasedStudent student = listBasedStudentRepository.findByIdWithCourses(studentId)
                .orElseThrow(EntityNotFoundException::new);

        Course course = student.getCourses()
                .stream().filter(c -> c.getId().equals(courseId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        course.changeName(newName);
    }

    @Transactional
    public void removeCourseFromStudent(Long studentId, Long courseId) {
        ListBasedStudent student = listBasedStudentRepository.findByIdWithCourses(studentId)
                .orElseThrow(EntityNotFoundException::new);
        Course courseToRemove = courseRepository.getCourseById(courseId);

        student.removeCourse(courseToRemove);
    }
}
