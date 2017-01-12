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
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("live")
public class PersonControllerHttpClientTest {

    private static final String URL = "http://localhost:8080/TestIntegrationShowcase/persons";

    private static ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldFindAllPersons() throws Exception {
        // given
        HttpPost addFirstRequest = new HttpPost(URL);
        addFirstRequest.addHeader("Content-Type", "application/json");
        HttpPost addSecondRequest = new HttpPost(URL);
        addSecondRequest.addHeader("Content-Type", "application/json");
        HttpGet getAllRequest = new HttpGet(URL);

        PersonDto personDtoFirst = createPerson("JohnDoe23@gmail.com", "111-11-1111");
        PersonDto personDtoSecond = createPerson("JohnDoe24@gmail.com", "111-11-1112");

        String jsonFirstPerson = objectMapper.writeValueAsString(personDtoFirst);
        addFirstRequest.setEntity(new StringEntity(jsonFirstPerson));
        String jsonSecondPerson = objectMapper.writeValueAsString(personDtoSecond);
        addSecondRequest.setEntity(new StringEntity(jsonSecondPerson));

        // when
        HttpResponse httpFirstResponse = HttpClientBuilder.create().build().execute(addFirstRequest);
        assertEquals(200, httpFirstResponse.getStatusLine().getStatusCode());
        PersonDto returnedFirstPerson = objectMapper.readValue(EntityUtils.toString(httpFirstResponse.getEntity()),
                PersonDto.class);
        assertEquals(personDtoFirst, returnedFirstPerson);

        HttpResponse httpSecondResponse = HttpClientBuilder.create().build().execute(addSecondRequest);
        assertEquals(200, httpSecondResponse.getStatusLine().getStatusCode());
        PersonDto returnedSecondPerson = objectMapper.readValue(EntityUtils.toString(httpSecondResponse.getEntity()),
                PersonDto.class);
        assertEquals(personDtoSecond, returnedSecondPerson);

        HttpResponse httpGetAllResponse = HttpClientBuilder.create().build().execute(getAllRequest);

        assertEquals(200, httpGetAllResponse.getStatusLine().getStatusCode());
        List<PersonDto> personList = objectMapper.readValue(EntityUtils.toString(httpGetAllResponse.getEntity()),
                new TypeReference<List<PersonDto>>() {});
        assertEquals(2, personList.size());
        assertTrue(personList.containsAll(Arrays.asList(personDtoFirst, personDtoSecond)));

    }

    @Test
    public void shouldAddPerson() throws Exception {
        // given
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");

        PersonDto person = createPerson("johndoe103@gmail.com", "789-67-1320");

        String jsonPerson = objectMapper.writeValueAsString(person);
        request.setEntity(new StringEntity(jsonPerson));

        // when
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        System.out.println(httpResponse.getAllHeaders());
        // then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        String jsonReturnedPerson = EntityUtils.toString(httpResponse.getEntity());
        PersonDto returnedPerson = objectMapper.readValue(jsonReturnedPerson, PersonDto.class);
        assertEquals(person, returnedPerson);
    }

    @Test
    public void shouldAddPersonAndCheckIfIsConsistent() throws Exception {
        // given
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");

        PersonDto person = createPerson("johndoe@gmail.com", "789-65-3271");

        String jsonPerson = objectMapper.writeValueAsString(person);
        request.setEntity(new StringEntity(jsonPerson));

        // when
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        // then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        String jsonReturnedPerson = EntityUtils.toString(httpResponse.getEntity());
        PersonDto returnedPerson = objectMapper.readValue(jsonReturnedPerson, PersonDto.class);
        assertEquals(person, returnedPerson);

        HttpGet getRequest = new HttpGet(URL + "/" + person.getSsn());

        // when
        HttpResponse httpGetResponse = HttpClientBuilder.create().build().execute(getRequest);
        assertEquals(200, httpGetResponse.getStatusLine().getStatusCode());
        String jsonReturnedGetPerson = EntityUtils.toString(httpGetResponse.getEntity());
        PersonDto returnedGetPerson = objectMapper.readValue(jsonReturnedGetPerson, PersonDto.class);
        assertEquals(returnedPerson, returnedGetPerson);

    }

    @Test
    public void shouldReturnNotFoundOnMissingSsn() throws Exception {
        // given
        String ssn = "123-12-1241";

        // when
        HttpGet request = new HttpGet(URL + "/" + ssn);

        // when
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        // then
        assertEquals(404, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        // given
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");

        PersonDto person = createPerson("johndoegmail.com", "789-67-3120");

        String jsonPerson = objectMapper.writeValueAsString(person);
        request.setEntity(new StringEntity(jsonPerson));

        // when
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        // then
        assertEquals(400, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldReturnBadRequestWhenSsnIsInvalid() throws Exception {
        // given
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");

        PersonDto person = createPerson("johndoe@gmail.com", "789-674-3");

        String jsonPerson = objectMapper.writeValueAsString(person);
        request.setEntity(new StringEntity(jsonPerson));

        // when
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        // then
        assertEquals(400, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldReturnServerErrorWhenPersonIsDuplicate() throws Exception {
        // given
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");

        PersonDto person = createPerson("johndoe104@gmail.com", "789-67-1320");

        String jsonPerson = objectMapper.writeValueAsString(person);
        request.setEntity(new StringEntity(jsonPerson));

        // when
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        // then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        String jsonReturnedPerson = EntityUtils.toString(httpResponse.getEntity());
        PersonDto returnedPerson = objectMapper.readValue(jsonReturnedPerson, PersonDto.class);
        assertEquals(person, returnedPerson);

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