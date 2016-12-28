package eu.execom.representation.spring_mvc_integration.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.execom.representation.spring_mvc_integration.dto.PersonDTO;
import eu.execom.representation.spring_mvc_integration.entity.Person;
import eu.execom.representation.spring_mvc_integration.repository.PersonRepository;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<PersonDTO> findAllPersons() {

        return personRepository.findAll()
                               .stream()
                               .map(person -> new PersonDTO(person))
                               .collect(Collectors.toList());
    }

    public PersonDTO getPersonBySSN(String SSN) {
        return new PersonDTO(personRepository.findBySSN(SSN));
    }

    public PersonDTO addPerson(PersonDTO personDTO) {
        return new PersonDTO(personRepository.save(new Person(personDTO)));
    }

}
