package com.hashedin.loginservice.service;

import com.google.gson.Gson;
import com.hashedin.loginservice.controller.LoginController;
import com.hashedin.loginservice.exception.ResetPasswordException;
import com.hashedin.loginservice.exception.UserAlreadyExistsException;
import com.hashedin.loginservice.exception.UserDoesNotExistException;
import com.hashedin.loginservice.model.User;
import com.hashedin.loginservice.repository.LoginRepository;
import com.hashedin.loginservice.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = LoginService.class)
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @MockBean
    private LoginRepository loginRepository;

    @MockBean
    private StockRepository stockRepository;

    @Test
    public void testLogin() throws Exception{

        User user = new User();
        user.setUsername("zain1234");
        user.setPassword("1234");

        Mockito.when(loginRepository.findByUsername(Mockito.anyString())).thenReturn(user);

        boolean login = loginService.login(user);
        assertEquals(true,login);
    }

    @Test
    public void testLogin2() throws Exception{

        User user = new User();

        Mockito.when(loginRepository.findByUsername(Mockito.anyString())).thenReturn(user);

        boolean login = loginService.login(user);
        assertEquals(false,login);
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

        Mockito.when(loginRepository.save(Mockito.any(User.class))).thenReturn(user);

        User signupUser = loginService.signUp(user);
        assertEquals(user.getUsername(),signupUser.getUsername());
    }

    @Test
    public void testSignUp_UserAlreadyPresent() throws Exception{

        User user = new User();
        user.setUsername("zain1234");
        user.setPassword("1234");
        user.setEmailId("zain@gmail.com");
        user.setPanNumber("123456789X");
        user.setSecurityQuestionIndex(1);
        user.setSecurityAnswer("ABC");

        Mockito.when(loginRepository.findByUsername(Mockito.anyString())).thenReturn(user);

        try {
            User signupUser = loginService.signUp(user);
        }
        catch (UserAlreadyExistsException userAlreadyExistsException){
            assertEquals("User already exists, please try log-in with that user",userAlreadyExistsException.getMessage());
        }
    }

    @Test
    public void testResetPassword() throws Exception{

        User user = new User();
        user.setUsername("zain1234");
        user.setPassword("1234");
        user.setEmailId("zain@gmail.com");
        user.setPanNumber("123456789X");
        user.setSecurityQuestionIndex(1);
        user.setSecurityAnswer("ABC");

        Mockito.when(loginRepository.save(Mockito.any(User.class))).thenReturn(user);

        User signupUser = loginService.signUp(user);
        assertEquals(user.getUsername(),signupUser.getUsername());
    }

    /*public boolean resetPassword(User user) throws UserDoesNotExistException {
        User userDetails = loginRepository.findByUsername(user.getUsername());
        if(userDetails==null) {
            throw new UserDoesNotExistException("User does not exist with the username, please retry with correct user.");
        }
        if(!user.getNewPassword().equals(user.getConfirmPassword())) {
            throw new ResetPasswordException("New password and Confirm password does not match.");
        }
        if(!validateSecurityQuestion(user,userDetails)) {
            throw new ResetPasswordException("Incorrect security answer, please try with the correct answer.");
        }
        if(user.getNewPassword().equals(userDetails.getPassword())){
            throw new ResetPasswordException("New Password entered matches with the old password, please enter a new password.");
        }

        userDetails.setPassword(user.getNewPassword());
        loginRepository.save(userDetails);
        return true;
    }*/


}