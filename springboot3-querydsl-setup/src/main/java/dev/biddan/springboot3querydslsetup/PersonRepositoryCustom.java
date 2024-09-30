package dev.biddan.springboot3querydslsetup;

import java.util.List;

public interface PersonRepositoryCustom {
    List<Person> findByNameAndAgeGreaterThan(String name, int age);
}
