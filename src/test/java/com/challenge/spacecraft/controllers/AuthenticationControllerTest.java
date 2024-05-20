package com.challenge.spacecraft.controllers;

import com.challenge.spacecraft.dtos.user.LoginUserDto;
import com.challenge.spacecraft.dtos.user.RegisterUserDto;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
class AuthenticationControllerTest {

    private static final String baseUrl = "/auth";

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void register() throws Exception {
        RegisterUserDto registerUserDto = buildRegisterUserDto(true, true, true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/signup")
                        .content(mapToJson(registerUserDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals( 200, mvcResult.getResponse().getStatus());
    }

    @Test
    void registerFailNoFullname() throws Exception {
        RegisterUserDto registerUserDto = buildRegisterUserDto(true, true,false);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/signup")
                .content(mapToJson(registerUserDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String errorMessage = JsonPath.parse(response).read("$.errors[0]");

        assertEquals( 400, mvcResult.getResponse().getStatus());
        assertEquals("Full name cannot be empty", errorMessage);
    }

    @Test
    void registerFailNoEmail() throws Exception {
        RegisterUserDto registerUserDto = buildRegisterUserDto(false, true,true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/signup")
                .content(mapToJson(registerUserDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String errorMessage = JsonPath.parse(response).read("$.errors[0]");

        assertEquals( 400, mvcResult.getResponse().getStatus());
        assertEquals("Email cannot be empty", errorMessage);
    }

    @Test
    void registerFailNoPassword() throws Exception {
        RegisterUserDto registerUserDto = buildRegisterUserDto(true, false,true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/signup")
                .content(mapToJson(registerUserDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String errorMessage = JsonPath.parse(response).read("$.errors[0]");

        assertEquals( 400, mvcResult.getResponse().getStatus());
        assertEquals("Password cannot be empty", errorMessage);
    }

    @Test
    void authenticate() throws Exception{
        LoginUserDto loginUserDto = buildLoginUserDto(true, true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                .content(mapToJson(loginUserDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals( 200, mvcResult.getResponse().getStatus());
    }

    @Test
    void authenticateFailNoEmail() throws Exception{
        LoginUserDto loginUserDto = buildLoginUserDto(false, true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                .content(mapToJson(loginUserDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String errorMessage = JsonPath.parse(response).read("$.errors[0]");

        assertEquals( 400, mvcResult.getResponse().getStatus());
        assertEquals("Email cannot be empty", errorMessage);
    }

    @Test
    void authenticateFailNoPassword() throws Exception{
        LoginUserDto loginUserDto = buildLoginUserDto(true, false);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                .content(mapToJson(loginUserDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String errorMessage = JsonPath.parse(response).read("$.errors[0]");

        assertEquals( 400, mvcResult.getResponse().getStatus());
        assertEquals("Password cannot be empty", errorMessage);
    }

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private RegisterUserDto buildRegisterUserDto(boolean setEmail, boolean setPassword ,boolean setFullName) {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        if(setEmail){
            registerUserDto.setEmail("email@email.com");
        }
        if(setPassword){
            registerUserDto.setPassword("password");
        }
        if(setFullName) {
            registerUserDto.setFullName("Test 1");
        }
        return registerUserDto;
    }

    private LoginUserDto buildLoginUserDto(boolean setEmail, boolean setPassword) {
        LoginUserDto loginUserDto = new LoginUserDto();

        if(setEmail){
            loginUserDto.setEmail("email@email.com");
        }

        if(setPassword){
            loginUserDto.setPassword("password");
        }
        return loginUserDto;
    }
}