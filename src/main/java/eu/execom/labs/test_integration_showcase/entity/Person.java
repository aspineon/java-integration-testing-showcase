package eu.execom.labs.test_integration_showcase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import eu.execom.labs.test_integration_showcase.dto.PersonDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long personId;

    @Column(nullable = false, unique = true)
    private String ssn;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    public Person(PersonDto personDto) {
        this.ssn = personDto.getSsn();
        this.email = personDto.getEmail();
        this.firstName = personDto.getFirstName();
        this.lastName = personDto.getLastName();
    }
}
