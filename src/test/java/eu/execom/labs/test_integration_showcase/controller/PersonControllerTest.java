package eu.execom.labs.test_integration_showcase.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.execom.labs.test_integration_showcase.configuration.TestRootConfiguration;
import eu.execom.labs.test_integration_showcase.configuration.TestWebConfiguration;
import eu.execom.labs.test_integration_showcase.dto.PersonDto;

@ContextConfiguration(classes = {TestRootConfiguration.class, TestWebConfiguration.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldFindAllPersons() throws Exception {
        PersonDto personDtoFirst = createPerson("johndoe101@gmail.com", "123-12-3334");
        PersonDto personDtoSecond = createPerson("johndoe102@gmail.com", "123-12-3345");

        // insert first person into db
        mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDtoFirst))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(personDtoFirst.getEmail())))
                .andExpect(jsonPath("$.ssn", is(personDtoFirst.getSsn())))
                .andExpect(jsonPath("$.firstName", is(personDtoFirst.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(personDtoFirst.getLastName())));

        // insert second person into db
        mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDtoSecond))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(personDtoSecond.getEmail())))
                .andExpect(jsonPath("$.ssn", is(personDtoSecond.getSsn())))
                .andExpect(jsonPath("$.firstName", is(personDtoSecond.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(personDtoSecond.getLastName())));

        // check if GET method returns objects that are initially added
        mockMvc.perform(get("/persons/")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].email", is(personDtoFirst.getEmail())))
                .andExpect(jsonPath("$[0].ssn", is(personDtoFirst.getSsn())))
                .andExpect(jsonPath("$[0].firstName", is(personDtoFirst.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(personDtoFirst.getLastName())))
                .andExpect(jsonPath("$[1].email", is(personDtoSecond.getEmail())))
                .andExpect(jsonPath("$[1].ssn", is(personDtoSecond.getSsn())))
                .andExpect(jsonPath("$[1].firstName", is(personDtoSecond.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(personDtoSecond.getLastName())));
    }

    @Test
    public void shouldAddPerson() throws Exception {
        PersonDto personDto = createPerson("johndoe@gmail.com", "123-45-6789");

        // insert person into db
        mockMvc.perform(post("/persons").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(personDto.getEmail())))
                .andExpect(jsonPath("$.ssn", is(personDto.getSsn())))
                .andExpect(jsonPath("$.firstName", is(personDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(personDto.getLastName())));
    }

    @Test
    public void shouldReturnServerErrorWhenPersonIsDuplicate() throws Exception {
        PersonDto personDto = createPerson("johndoe2@gmail.com", "123-45-6780");

        // insert person into db
        mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDto))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(personDto.getEmail())))
                .andExpect(jsonPath("$.ssn", is(personDto.getSsn())))
                .andExpect(jsonPath("$.firstName", is(personDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(personDto.getLastName())));

        // try to insert duplicate person
        mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDto))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
    }

    @Test
    public void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        PersonDto personDto = createPerson("johndoegmail.com", "123-45-6789");

        // try to insert person with incorrect email
        mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDto))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnBadRequestWhenSsnIsInvalid() throws Exception {
        PersonDto personDto = createPerson("johndoe@gmail.com", "123-45-6");

        // try to insert person with incorrect ssn
        mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDto))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddPersonAndCheckIfIsConsistent() throws Exception {
        PersonDto personDto = createPerson("johndoe3@gmail.com", "123-45-6781");

        // insert person
        mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDto))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(personDto.getEmail())))
                .andExpect(jsonPath("$.ssn", is(personDto.getSsn())))
                .andExpect(jsonPath("$.firstName", is(personDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(personDto.getLastName())));

        // check if inserted person is same as the one from GET request
        mockMvc.perform(get("/persons/" + personDto.getSsn())).andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(personDto.getEmail())))
                .andExpect(jsonPath("$.ssn", is(personDto.getSsn())))
                .andExpect(jsonPath("$.firstName", is(personDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(personDto.getLastName())));
    }

    @Test
    public void shouldReturnNotFoundOnMissingSsn() throws Exception {
        String ssn = "123-45-9999";

        // check if person with provided ssn exists in db (should return bad request)
        mockMvc.perform(get("/person/" + ssn)).andExpect(status().is4xxClientError());
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