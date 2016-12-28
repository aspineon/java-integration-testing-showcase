package eu.execom.labs.test_integration_showcase.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.labs.test_integration_showcase.dto.PersonDto;
import eu.execom.labs.test_integration_showcase.entity.Person;
import eu.execom.labs.test_integration_showcase.repository.PersonRepository;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<PersonDto> findAllPersons() {
        return personRepository.findAll()
                               .stream()
                               .map(person -> new PersonDto(person))
                               .collect(Collectors.toList());
    }

    public PersonDto getPersonBySsn(String ssn) {
        return new PersonDto(personRepository.findBySsn(ssn));
    }

    public PersonDto addPerson(PersonDto personDto) {
        return new PersonDto(personRepository.save(new Person(personDto)));
    }

}
