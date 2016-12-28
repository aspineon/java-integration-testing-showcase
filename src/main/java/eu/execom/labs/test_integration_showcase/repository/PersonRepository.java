package eu.execom.labs.test_integration_showcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.labs.test_integration_showcase.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findBySsn(String ssn);

}
