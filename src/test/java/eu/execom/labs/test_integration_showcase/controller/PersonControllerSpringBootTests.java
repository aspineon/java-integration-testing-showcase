package eu.execom.labs.test_integration_showcase.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.execom.labs.test_integration_showcase.configuration.TestRootConfiguration;
import eu.execom.labs.test_integration_showcase.configuration.TestWebConfiguration;
import eu.execom.labs.test_integration_showcase.dto.PersonDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestRootConfiguration.class,
        TestWebConfiguration.class})
@EnableAutoConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonControllerSpringBootTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldAddPerson() throws Exception {
        PersonDto personDto = createPerson("JohnDoe23@gmail.com", "111-11-1111");

        // create a new request with the entity body (personDto)
        HttpEntity<PersonDto> request = new HttpEntity<>(personDto);

        // execute request (add person) and get response
        ResponseEntity<String> response = restTemplate.postForEntity("/persons", request, String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // check equality of expected (initially added) and returned person
        PersonDto person = objectMapper.readValue(response.getBody(), new TypeReference<PersonDto>() {});
        assertEquals(personDto, person);
    }

    @Test
    public void shouldFindAllPersons() throws Exception {
        PersonDto personDtoFirst = createPerson("JohnDoe24@gmail.com", "111-11-1112");
        PersonDto personDtoSecond = createPerson("JohnDoe25@gmail.com", "111-11-1113");

        // create a new request with the entity body (personDto)
        HttpEntity<PersonDto> requestFirst = new HttpEntity<>(personDtoFirst);
        HttpEntity<PersonDto> requestSecond = new HttpEntity<>(personDtoSecond);

        // execute request (add first person) and get response
        ResponseEntity<String> addFirstResponse = restTemplate.postForEntity("/persons", requestFirst, String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.OK, addFirstResponse.getStatusCode());

        // check equality of expected (initially added first person) and returned person
        PersonDto returnedFirstPerson = objectMapper.readValue(addFirstResponse.getBody(),
                new TypeReference<PersonDto>() {});
        assertEquals(personDtoFirst, returnedFirstPerson);

        // execute request (add second person) and get response
        ResponseEntity<String> addSecondResponse = restTemplate.postForEntity("/persons", requestSecond, String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.OK, addSecondResponse.getStatusCode());

        // check equality of expected (initially added second person) and returned person
        PersonDto returnedSecondPerson = objectMapper.readValue(addSecondResponse.getBody(),
                new TypeReference<PersonDto>() {});
        assertEquals(personDtoSecond, returnedSecondPerson);

        // execute request (get all persons) and get response
        ResponseEntity<String> getAllResponse = restTemplate.getForEntity("/persons", String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());

        // check equality of expected (initially added) person list size and actual list size
        List<PersonDto> personList = objectMapper.readValue(getAllResponse.getBody(),
                new TypeReference<List<PersonDto>>() {});
        assertEquals(2, personList.size());

        // check if response contains objects that are initially added
        assertTrue(personList.containsAll(Arrays.asList(personDtoFirst, personDtoSecond)));
    }

    @Test
    public void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        PersonDto personDto = createPerson("JohnDoe29gmail.com", "211-11-1121");

        // create a new request with the entity body (personDto)
        HttpEntity<PersonDto> request = new HttpEntity<>(personDto);

        // execute request (try to add person) and get response
        ResponseEntity<String> response = restTemplate.postForEntity("/persons", request, String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldReturnBadRequestWhenSsnIsInvalid() throws Exception {
        PersonDto personDto = createPerson("JohnDoe25@gmail.com", "311-111");

        // create a new request with the entity body (personDto)
        HttpEntity<PersonDto> request = new HttpEntity<>(personDto);

        // execute request (try to add person) and get response
        ResponseEntity<String> response = restTemplate.postForEntity("/persons", request, String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldAddPersonAndCheckIfIsConsistent() throws Exception {
        PersonDto personDto = createPerson("JohnDoe26@gmail.com", "111-11-1116");

        // create a new request with the entity body (personDto)
        HttpEntity<PersonDto> request = new HttpEntity<>(personDto);

        // execute request (add person) and get response
        ResponseEntity<String> addResponse = restTemplate.postForEntity("/persons", request, String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.OK, addResponse.getStatusCode());

        // check equality of expected (initially added person) and returned person
        PersonDto returnedPerson = objectMapper.readValue(addResponse.getBody(), PersonDto.class);
        assertEquals(personDto, returnedPerson);

        // execute request (get initially added person) and get response
        ResponseEntity<String> getResponse = restTemplate.getForEntity("/persons/" + returnedPerson.getSsn(),
                String.class);

        // check equality of expected and returned person
        PersonDto resultPerson = objectMapper.readValue(getResponse.getBody(), PersonDto.class);
        assertEquals(returnedPerson, resultPerson);
    }

    @Test
    public void shouldReturnServerErrorWhenEmailIsDuplicate() throws Exception {
        PersonDto personDto = createPerson("JohnDoe27@gmail.com", "511-11-1114");

        // create a new request with the entity body (personDto)
        HttpEntity<PersonDto> request = new HttpEntity<>(personDto);

        // execute request (add person) and get response
        ResponseEntity<String> response = restTemplate.postForEntity("/persons", request, String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // check equality of expected (initially added person) and returned person
        PersonDto returnedPerson = objectMapper.readValue(response.getBody(), new TypeReference<PersonDto>() {});
        assertEquals(personDto, returnedPerson);

        // execute request (try to add person with duplicate email) and get response
        personDto.setSsn("111-22-1414");
        ResponseEntity<String> duplicateResponse = restTemplate.postForEntity("/persons", request, String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, duplicateResponse.getStatusCode());
    }

    @Test
    public void shouldReturnServerErrorWhenSsnIsDuplicate() throws Exception {
        PersonDto personDto = createPerson("JohnDoe27@gmail.com", "511-11-1114");

        // create a new request with the entity body (personDto)
        HttpEntity<PersonDto> request = new HttpEntity<>(personDto);

        // execute request (add person) and get response
        ResponseEntity<String> response = restTemplate.postForEntity("/persons", request, String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // check equality of expected (initially added person) and returned person
        PersonDto returnedPerson = objectMapper.readValue(response.getBody(), new TypeReference<PersonDto>() {});
        assertEquals(personDto, returnedPerson);

        // execute request (try to add person with duplicate ssn) and get response
        personDto.setEmail("JohnDoe28@gmail.com");
        ResponseEntity<String> duplicateResponse = restTemplate.postForEntity("/persons", request, String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, duplicateResponse.getStatusCode());
    }

    @Test
    public void shouldReturnNotFoundOnMissingSsn() throws Exception {
        String ssn = "999-99-9999";

        // execute request (check if person with provided ssn exists in db) and get response
        ResponseEntity<String> response = restTemplate.getForEntity("/persons/" + ssn, String.class);

        // check equality of expected and actual status code
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private PersonDto createPerson(String email, String ssn) {
        PersonDto personDto = new PersonDto();
        personDto.setEmail(email);
        personDto.setFirstName("John");
        personDto.setLastName("Doe");
        personDto.setSsn(ssn);
        return personDto;
    }
}
