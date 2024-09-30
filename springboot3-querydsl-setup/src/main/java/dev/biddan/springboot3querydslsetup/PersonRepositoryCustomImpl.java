package dev.biddan.springboot3querydslsetup;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersonRepositoryCustomImpl implements PersonRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Person> findByNameAndAgeGreaterThan(String name, int age) {
        QPerson person = QPerson.person;
        return jpaQueryFactory
                .selectFrom(person)
                .where(person.name.eq(name)
                        .and(person.age.gt(age)))
                .fetch();
    }
}
