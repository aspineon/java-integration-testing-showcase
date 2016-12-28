package eu.execom.representation.spring_mvc_integration.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.execom.representation.spring_mvc_integration.dto.PersonDTO;
import eu.execom.representation.spring_mvc_integration.entity.Person;

@RestController
public class PersonController {

    @RequestMapping("/add")
    public Person addPerson(PersonDTO person) {
        // TODO implement
        return new Person();

    }

    @RequestMapping("/persons")
    public Person findAll() {
        // TODO implement
        return new Person();

    }

    @RequestMapping("/getPerson")
    public Person getPersonBySSN(PersonDTO person) {
        // TODO implement
        return new Person();

    }
}