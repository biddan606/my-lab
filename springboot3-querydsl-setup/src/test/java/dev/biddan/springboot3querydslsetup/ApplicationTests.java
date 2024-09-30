package dev.biddan.springboot3querydslsetup;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ApplicationTests {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testQueryDsl() {
        // Given
        Person person1 = Person.builder()
                .name("John")
                .age(30)
                .build();
        personRepository.save(person1);

        Person person2 = Person.builder()
                .name("John")
                .age(40)
                .build();
        personRepository.save(person2);

        Person person3 = Person.builder()
                .name("Jane")
                .age(35)
                .build();
        personRepository.save(person3);

        // When
        List<Person> actual = personRepository.findByNameAndAgeGreaterThan("John", 35);

        // Then
        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst().getName()).isEqualTo("John");
        assertThat(actual.getFirst().getAge()).isEqualTo(40);
    }
}
