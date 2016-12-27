package eu.execom.representation.spring_mvc_integration.dto;

import eu.execom.representation.spring_mvc_integration.entity.Person;
import lombok.Data;

@Data
public class PersonDTO {

    private long personId;

    private String SSN;

    private String firstName;

    private String lastName;

    private String email;

    public PersonDTO(Person person) {
        this.personId = person.getPersonId();
        this.SSN = person.getSSN();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
    }

}
