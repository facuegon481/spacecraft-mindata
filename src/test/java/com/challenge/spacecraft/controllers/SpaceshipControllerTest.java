package com.challenge.spacecraft.controllers;

import com.challenge.spacecraft.dtos.spacecraft.CreateSpacecraftDto;
import com.challenge.spacecraft.dtos.spacecraft.UpdateSpacecraftDto;
import com.challenge.spacecraft.dtos.user.LoginUserDto;
import com.challenge.spacecraft.entities.Spaceship;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@WebAppConfiguration
class SpaceshipControllerTest {

    private static final String baseUrl = "/spaceships";
    private String token;
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.token = this.login();
    }

    @Test
    void getSpaceships() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl)
                .header("Authorization", "Bearer " + this.token)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn()
                ;

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Spaceship> spaceships = objectMapper.readValue(jsonResponse, new TypeReference<List<Spaceship>>() {});

        assertEquals( 200, mvcResult.getResponse().getStatus());
        assertFalse(spaceships.isEmpty());
    }

    @Test
    void getSpaceship() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/1")
                .header("Authorization", "Bearer " + this.token)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn()
                ;

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Spaceship spaceship = objectMapper.readValue(jsonResponse, Spaceship.class);

        assertEquals( 200, mvcResult.getResponse().getStatus());
        assertNotNull(spaceship);
    }

    @Test
    void getSpaceshipNegativeID() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/-1")
                .header("Authorization", "Bearer " + this.token)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn()
                ;

        String response = mvcResult.getResponse().getContentAsString();
        String errorMessage = JsonPath.parse(response).read("$.message");
        assertEquals( 400, mvcResult.getResponse().getStatus());
        assertEquals("Spaceship id must be greater than 0", errorMessage);
    }

    @Test
    void createSpaceship() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .header("Authorization", "Bearer " + this.token)
                .content(mapToJson(buildCreateSpacecraftDto("ship created")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn()
                ;

        assertEquals( 200, mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteSpaceship() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/1")
                .header("Authorization", "Bearer " + this.token)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn()
                ;

        assertEquals( 200, mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteSpaceshipNegativeID() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/-1")
                .header("Authorization", "Bearer " + this.token)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn()
                ;

        String response = mvcResult.getResponse().getContentAsString();
        String errorMessage = JsonPath.parse(response).read("$.message");
        assertEquals( 400, mvcResult.getResponse().getStatus());
        assertEquals("Spaceship id must be greater than 0", errorMessage);
    }

    @Test
    void updateSpaceship() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/3")
                .header("Authorization", "Bearer " + this.token)
                .content(mapToJson(buildUpdateSpacecraftDto("ship updated")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn()
                ;
        assertEquals( 200, mvcResult.getResponse().getStatus());
    }

    @Test
    void updateSpaceshipNegativeID() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/-1")
                .header("Authorization", "Bearer " + this.token)
                .content(mapToJson(buildUpdateSpacecraftDto("ship updated")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn()
                ;

        String response = mvcResult.getResponse().getContentAsString();
        String errorMessage = JsonPath.parse(response).read("$.message");

        assertEquals( 400, mvcResult.getResponse().getStatus());
        assertEquals("Spaceship id must be greater than 0", errorMessage);
    }

    private String login() throws Exception {
        LoginUserDto loginUserDto = buildLoginUserDto();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .content(mapToJson(loginUserDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        return JsonPath.parse(response).read("$.token");
    }

    private LoginUserDto buildLoginUserDto() {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("admin@admin.com");
        loginUserDto.setPassword("q1w2e3r4");
        return loginUserDto;
    }

    private CreateSpacecraftDto buildCreateSpacecraftDto(String spacecraftName) {
        CreateSpacecraftDto spacecraftDto = new CreateSpacecraftDto();
        spacecraftDto.setName(spacecraftName);
        return spacecraftDto;
    }

    private UpdateSpacecraftDto buildUpdateSpacecraftDto(String spacecraftName) {
        UpdateSpacecraftDto spacecraftDto = new UpdateSpacecraftDto();
        spacecraftDto.setName(spacecraftName);
        return spacecraftDto;
    }

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}