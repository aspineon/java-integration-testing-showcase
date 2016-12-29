package eu.execom.labs.test_integration_showcase.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.execom.labs.test_integration_showcase.dto.PersonDto;
import eu.execom.labs.test_integration_showcase.service.PersonService;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping
    public PersonDto addPerson(@Valid @RequestBody PersonDto person) {
        return personService.addPerson(person);
    }

    @GetMapping
    public List<PersonDto> findAll() {
        return personService.findAllPersons();
    }

    @GetMapping("/{ssn}")
    public PersonDto getPersonBySsn(@PathVariable String ssn) {
        return personService.getPersonBySsn(ssn);
    }
}