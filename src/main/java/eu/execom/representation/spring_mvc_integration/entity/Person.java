package eu.execom.representation.spring_mvc_integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import eu.execom.representation.spring_mvc_integration.dto.PersonDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long personId;

    @Column(nullable = false, unique = true)
    private String SSN;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    public Person(PersonDTO personDTO) {
        this.SSN = personDTO.getSSN();
        this.email = personDTO.getEmail();
        this.firstName = personDTO.getFirstName();
        this.lastName = personDTO.getLastName();

    }

}
