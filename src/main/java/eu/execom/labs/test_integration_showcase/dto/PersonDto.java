package eu.execom.labs.test_integration_showcase.dto;

import eu.execom.labs.test_integration_showcase.entity.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDto {

    private long personId;

    private String ssn;

    private String email;

    private String firstName;

    private String lastName;

    public PersonDto(Person person) {
        this.personId = person.getPersonId();
        this.ssn = person.getSsn();
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
    }

}
