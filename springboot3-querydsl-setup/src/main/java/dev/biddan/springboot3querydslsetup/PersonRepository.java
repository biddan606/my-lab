package dev.biddan.springboot3querydslsetup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PersonRepository extends JpaRepository<Person, Long>, PersonRepositoryCustom {
}
