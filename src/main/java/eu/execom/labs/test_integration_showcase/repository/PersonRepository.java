package eu.execom.labs.test_integration_showcase.repository;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.labs.test_integration_showcase.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findBySsn(String ssn) throws NoSuchElementException;

}
