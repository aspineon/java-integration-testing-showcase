package eu.execom.labs.test_integration_showcase.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.execom.labs.test_integration_showcase.dto.PersonDto;
import eu.execom.labs.test_integration_showcase.entity.Person;

@RestController
public class PersonController {

    @RequestMapping("/add")
    public PersonDto addPerson(PersonDto person) {
        // TODO implement
        return new PersonDto(new Person());

    }

    @RequestMapping("/persons")
    public List<Person> findAll() {
        // TODO implement
        return new ArrayList<Person>();

    }

    @RequestMapping("/getPerson")
    public PersonDto getPersonBySsn(PersonDto person) {
        // TODO implement
        return new PersonDto(new Person());

    }
}