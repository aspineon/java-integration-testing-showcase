package eu.execom.labs.test_integration_showcase.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.execom.labs.test_integration_showcase.dto.PersonDto;
import eu.execom.labs.test_integration_showcase.service.PersonService;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping("/add")
    public PersonDto addPerson(PersonDto person) {
        return personService.addPerson(person);
    }

    @RequestMapping("/persons")
    public List<PersonDto> findAll() {
        return personService.findAllPersons();
    }

    @RequestMapping("/getPerson")
    public PersonDto getPersonBySsn(String ssn) {
        return personService.getPersonBySsn(ssn);
    }
}