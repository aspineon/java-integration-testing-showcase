package eu.execom.labs.test_integration_showcase.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.execom.labs.test_integration_showcase.configuration.RootConfiguration;
import eu.execom.labs.test_integration_showcase.configuration.WebConfiguration;
import eu.execom.labs.test_integration_showcase.dto.PersonDto;

@ContextConfiguration(classes = {RootConfiguration.class, WebConfiguration.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonControllerHttpClientTest {

    private static final String URL = "http://localhost:8080/TestIntegrationShowcase/persons";

    private static ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldFindAllPersons() throws Exception {
        // create HTTP requests
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");
        HttpGet getAllRequest = new HttpGet(URL);

        // create person and set it to request entity
        PersonDto personDtoFirst = createPerson("JohnDoe23@gmail.com", "111-11-1111");
        request.setEntity(new StringEntity(objectMapper.writeValueAsString(personDtoFirst)));

        // execute request and assert that status code is OK
        HttpResponse httpFirstResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(200, httpFirstResponse.getStatusLine().getStatusCode());

        // deserialize response to PersonDto object and assert equality of objects
        PersonDto returnedFirstPerson = objectMapper.readValue(EntityUtils.toString(httpFirstResponse.getEntity()),
                PersonDto.class);
        assertEquals(personDtoFirst, returnedFirstPerson);

        // create person and set it to request entity
        PersonDto personDtoSecond = createPerson("JohnDoe24@gmail.com", "111-11-1112");
        request.setEntity(new StringEntity(objectMapper.writeValueAsString(personDtoSecond)));

        // execute request and assert that status code is OK
        HttpResponse httpSecondResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(200, httpSecondResponse.getStatusLine().getStatusCode());

        // deserialize response to PersonDto object and assert equality of objects
        PersonDto returnedSecondPerson = objectMapper.readValue(EntityUtils.toString(httpSecondResponse.getEntity()),
                PersonDto.class);
        assertEquals(personDtoSecond, returnedSecondPerson);

        // get all persons from db and assert their equality with the ones inserted
        HttpResponse httpGetAllResponse = HttpClientBuilder.create().build().execute(getAllRequest);
        assertEquals(200, httpGetAllResponse.getStatusLine().getStatusCode());

        List<PersonDto> personList = objectMapper.readValue(EntityUtils.toString(httpGetAllResponse.getEntity()),
                new TypeReference<List<PersonDto>>() {});
        assertEquals(2, personList.size());
        assertTrue(personList.containsAll(Arrays.asList(personDtoFirst, personDtoSecond)));
    }

    @Test
    public void shouldAddPerson() throws Exception {
        // create HTTP request
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");

        // create person and set it to request entity
        PersonDto person = createPerson("johndoe103@gmail.com", "789-67-1320");
        request.setEntity(new StringEntity(objectMapper.writeValueAsString(person)));

        // execute request and assert that status code is OK
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());

        // deserialize response to PersonDto object and assert equality of objects
        PersonDto returnedPerson = objectMapper.readValue(EntityUtils.toString(httpResponse.getEntity()),
                PersonDto.class);
        assertEquals(person, returnedPerson);
    }

    @Test
    public void shouldAddPersonAndCheckIfIsConsistent() throws Exception {
        // create HTTP request
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");

        // create person and set it to request entity
        PersonDto person = createPerson("johndoe@gmail.com", "789-65-3271");
        request.setEntity(new StringEntity(objectMapper.writeValueAsString(person)));

        // execute request and assert that status code is OK
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());

        // deserialize response to PersonDto object and assert equality of objects
        PersonDto returnedPerson = objectMapper.readValue(EntityUtils.toString(httpResponse.getEntity()),
                PersonDto.class);
        assertEquals(person, returnedPerson);

        // get person from db
        HttpGet getRequest = new HttpGet(URL + "/" + person.getSsn());
        HttpResponse httpGetResponse = HttpClientBuilder.create().build().execute(getRequest);
        assertEquals(200, httpGetResponse.getStatusLine().getStatusCode());

        // assert equality between inserted and retrieved objects
        String jsonReturnedGetPerson = EntityUtils.toString(httpGetResponse.getEntity());
        PersonDto returnedGetPerson = objectMapper.readValue(jsonReturnedGetPerson, PersonDto.class);
        assertEquals(returnedPerson, returnedGetPerson);
    }

    @Test
    public void shouldReturnNotFoundOnMissingSsn() throws Exception {
        String ssn = "123-12-1241";
        HttpGet request = new HttpGet(URL + "/" + ssn);

        // execute request with unknown ssn and expect to return status code 404 Not Found
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(404, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");

        // create person and set it to request entity
        String jsonPerson = objectMapper.writeValueAsString(createPerson("johndoegmail.com", "789-67-3120"));
        request.setEntity(new StringEntity(jsonPerson));

        // execute request with invalid email and expect to return status code 404 Not Found
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(400, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldReturnBadRequestWhenSsnIsInvalid() throws Exception {
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");

        // create person and set it to request entity
        String jsonPerson = objectMapper.writeValueAsString(createPerson("johndoe@gmail.com", "789-674-3"));
        request.setEntity(new StringEntity(jsonPerson));

        // execute request with invalid ssn and expect to return status code 404 Not Found
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(400, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldReturnServerErrorWhenEmailIsDuplicate() throws Exception {
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");

        // create person and set it to request entity
        PersonDto person = createPerson("johndoe@gmail.com", "123-45-6789");
        String jsonPerson = objectMapper.writeValueAsString(person);
        request.setEntity(new StringEntity(jsonPerson));

        // execute request and assert that status code is OK
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());

        // assert equality between inserted and retrieved objects
        String jsonReturnedPerson = EntityUtils.toString(httpResponse.getEntity());
        PersonDto returnedPerson = objectMapper.readValue(jsonReturnedPerson, PersonDto.class);
        assertEquals(person, returnedPerson);

        // create person with duplicate email, execute request and assert that status code is OK
        person.setSsn("987-65-4321");
        String jsonDuplicatePerson = objectMapper.writeValueAsString(person);
        request.setEntity(new StringEntity(jsonDuplicatePerson));
        HttpResponse httpResponseDuplicate = HttpClientBuilder.create().build().execute(request);
        assertEquals(500, httpResponseDuplicate.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldReturnServerErrorWhenSsnIsDuplicate() throws Exception {
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");

        // create person and set it to request entity
        PersonDto person = createPerson("johndoe@gmail.com", "123-45-6789");
        String jsonPerson = objectMapper.writeValueAsString(person);
        request.setEntity(new StringEntity(jsonPerson));

        // execute request and assert that status code is OK
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());

        // assert equality between inserted and retrieved objects
        String jsonReturnedPerson = EntityUtils.toString(httpResponse.getEntity());
        PersonDto returnedPerson = objectMapper.readValue(jsonReturnedPerson, PersonDto.class);
        assertEquals(person, returnedPerson);

        // create person with duplicate email, execute request and assert that status code is OK
        person.setEmail("johndoe2@hotmail.com");
        String jsonDuplicatePerson = objectMapper.writeValueAsString(person);
        request.setEntity(new StringEntity(jsonDuplicatePerson));
        HttpResponse httpResponseDuplicate = HttpClientBuilder.create().build().execute(request);
        assertEquals(500, httpResponseDuplicate.getStatusLine().getStatusCode());
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