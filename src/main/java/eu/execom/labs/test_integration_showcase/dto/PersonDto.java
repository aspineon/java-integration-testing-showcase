package eu.execom.labs.test_integration_showcase.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;

import eu.execom.labs.test_integration_showcase.entity.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDto {

    private long personId;

    @NotNull
    @Pattern(regexp = ("[0-3]{3}-[0-3]{3}-[0-3]{3}"))
    private String ssn;

    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^\\w+( \\w*)*$")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^\\w+( \\w*)*$")
    private String lastName;

    public PersonDto(Person person) {
        this.personId = person.getPersonId();
        this.ssn = person.getSsn();
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
    }

}
