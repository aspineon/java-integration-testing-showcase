package eu.execom.representation.spring_mvc_integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.representation.spring_mvc_integration.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findBySSN(String SSN);

}
