package com.hashedin.loginservice.controller;

import com.google.gson.Gson;
import com.hashedin.loginservice.exception.UserAlreadyLoggedInException;
import com.hashedin.loginservice.model.User;
import com.hashedin.loginservice.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Headers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.mockito.Mockito.when;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Test
    public void testLogin() throws Exception{

        User user = new User();
        user.setUsername("zain1234");
        user.setPassword("1234");

        String inputJson = new Gson().toJson(user);
        String URI = "/loginUser/login";

        Mockito.when(loginService.login(Mockito.any(User.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URI).accept(MediaType.APPLICATION_JSON).content(inputJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String outputJson = mvcResult.getResponse().getContentAsString();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testSignUp() throws Exception{
        User user = new User();
        user.setUsername("zain1234");
        user.setPassword("1234");
        user.setEmailId("zain@gmail.com");
        user.setPanNumber("123456789X");
        user.setSecurityQuestionIndex(1);
        user.setSecurityAnswer("ABC");

        String inputJson = new Gson().toJson(user);
        String URI = "/loginUser/signup";

        Mockito.when(loginService.signUp(Mockito.any(User.class))).thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URI).accept(MediaType.APPLICATION_JSON).content(inputJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String outputJson = mvcResult.getResponse().getContentAsString();
        assertEquals(201,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testResetPassword() throws Exception{
        User user = new User();
        user.setUsername("zain1234");
        user.setNewPassword("12345");
        user.setConfirmPassword("12345");
        user.setSecurityQuestionIndex(1);
        user.setSecurityAnswer("ABC");

        String inputJson = new Gson().toJson(user);
        String URI = "/loginUser/resetPassword";

        Mockito.when(loginService.resetPassword(Mockito.any(User.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URI).accept(MediaType.APPLICATION_JSON).content(inputJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String outputJson = mvcResult.getResponse().getContentAsString();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    void contextLoads() {
    }
}