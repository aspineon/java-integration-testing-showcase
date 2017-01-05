package eu.execom.labs.test_integration_showcase.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.charset.Charset;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.execom.labs.test_integration_showcase.configuration.RootConfigTest;
import eu.execom.labs.test_integration_showcase.configuration.WebConfigTest;
import eu.execom.labs.test_integration_showcase.dto.PersonDto;

@ContextConfiguration(classes = {RootConfigTest.class, WebConfigTest.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class PersonControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                 .build();
        objectMapper = new ObjectMapper();
    }

    public PersonDto createPerson(String email, String ssn) {
        PersonDto personDto = new PersonDto();
        personDto.setEmail(email);
        personDto.setFirstName("John");
        personDto.setLastName("Doe");
        personDto.setSsn(ssn);
        return personDto;
    }

    @Test
    public void shouldFindAllPersons() throws Exception {
        // given
        PersonDto personDtoFirst = createPerson("johndoe101@gmail.com", "123-123-333");
        PersonDto personDtoSecond = createPerson("johndoe102@gmail.com", "123-123-334");

        // when
        MvcResult mockResultFirst = mockMvc.perform(
                post("/persons").content(objectMapper.writeValueAsString(personDtoFirst))
                                .contentType(contentType))
                                           .andReturn();
        MvcResult mockResultSecond = mockMvc.perform(
                post("/persons").content(objectMapper.writeValueAsString(personDtoSecond))
                                .contentType(contentType))
                                            .andReturn();
        assertEquals(mockResultFirst.getResponse()
                                    .getStatus(),
                200);
        assertEquals(mockResultSecond.getResponse()
                                     .getStatus(),
                200);

        // then
        MvcResult result = mockMvc.perform(get("/persons"))
                                  .andReturn();
        String returnedResult = result.getResponse()
                                      .getContentAsString();
        List<PersonDto> personList = objectMapper.readValue(returnedResult, new TypeReference<List<PersonDto>>() {
        });
        assertEquals(personList.size(), 2);
    }

    @Test
    public void shouldAddPerson() throws Exception {
        // given
        PersonDto personDto = createPerson("johndoe23@gmail.com", "123-123-456");

        // when
        MvcResult mockResult = mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDto))
                                                               .contentType(contentType))
                                      .andReturn();

        // then
        String jsonResult = mockResult.getResponse()
                                      .getContentAsString();
        PersonDto returnedPerson = objectMapper.readValue(jsonResult, PersonDto.class);
        assertEquals(personDto, returnedPerson);
        assertEquals(mockResult.getResponse()
                               .getStatus(),
                200);
    }

    @Test
    public void shouldAddDuplicatePerson() throws Exception {
        // given
        PersonDto personDto = createPerson("johndoe23@gmail.com", "123-123-456");

        // when
        MvcResult mockResult = mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDto))
                                                               .contentType(contentType))
                                      .andReturn();

        // then
        String jsonResult = mockResult.getResponse()
                                      .getContentAsString();
        PersonDto returnedPerson = objectMapper.readValue(jsonResult, PersonDto.class);
        assertEquals(personDto, returnedPerson);
        assertEquals(mockResult.getResponse()
                               .getStatus(),
                200);

        MvcResult mockResultReturn = mockMvc.perform(
                post("/persons").content(objectMapper.writeValueAsString(personDto))
                                .contentType(contentType))
                                            .andReturn();
        assertEquals(mockResultReturn.getResponse()
                                     .getStatus(),
                500);

    }

    @Test
    public void shouldAddPersonWhenEmailIsNotCorrect() throws Exception {
        // given
        PersonDto personDto = createPerson("johndoe23gmail.com", "123-123-456");

        // when
        MvcResult mockResult = mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDto))
                                                               .contentType(contentType))
                                      .andReturn();
        // then
        assertEquals(mockResult.getResponse()
                               .getStatus(),
                400);
    }

    @Test
    public void shouldAddPersonWhenSsnIsNotCorrect() throws Exception {
        // given
        PersonDto personDto = createPerson("johndoe23@gmail.com", "123-123-4");

        // when
        MvcResult mockResult = mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDto))
                                                               .contentType(contentType))
                                      .andReturn();
        // then
        assertEquals(mockResult.getResponse()
                               .getStatus(),
                400);
    }

    @Test
    public void shouldAddPersonAndCheckIfIsConsistent() throws Exception {
        // given
        PersonDto personDto = createPerson("johndoe24@gmail.com", "123-123-457");

        // when
        MvcResult mockAddResult = mockMvc.perform(post("/persons").content(objectMapper.writeValueAsString(personDto))
                                                                  .contentType(contentType))
                                         .andReturn();

        // then
        String jsonAddResult = mockAddResult.getResponse()
                                            .getContentAsString();
        PersonDto returnedPerson = objectMapper.readValue(jsonAddResult, PersonDto.class);
        assertEquals(personDto, returnedPerson);
        assertEquals(mockAddResult.getResponse()
                                  .getStatus(),
                200);

        MvcResult mockGetResult = mockMvc.perform(get("/persons/" + returnedPerson.getSsn()))
                                         .andReturn();
        String jsonGetResult = mockGetResult.getResponse()
                                            .getContentAsString();
        PersonDto resultPerson = objectMapper.readValue(jsonGetResult, PersonDto.class);
        assertEquals(returnedPerson, resultPerson);
    }

    @Test
    public void shouldGetOnePersonNotFound() throws Exception {
        // given
        String ssn = "123-123-123";

        // when
        MvcResult mockResult = mockMvc.perform(get("/persons/" + ssn))
                                      .andReturn();

        // then
        assertEquals(mockResult.getResponse()
                               .getStatus(),
                404);
    }
}