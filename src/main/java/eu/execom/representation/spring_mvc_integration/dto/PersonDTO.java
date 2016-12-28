package eu.execom.representation.spring_mvc_integration.dto;

import eu.execom.representation.spring_mvc_integration.entity.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDTO {

    private long personId;

    private String SSN;

    private String email;

    private String firstName;

    private String lastName;

    public PersonDTO(Person person) {
        this.personId = person.getPersonId();
        this.SSN = person.getSSN();
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();

    }

}
