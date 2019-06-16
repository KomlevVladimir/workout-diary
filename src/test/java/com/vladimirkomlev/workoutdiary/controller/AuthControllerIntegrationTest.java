package com.vladimirkomlev.workoutdiary.controller;

import com.vladimirkomlev.workoutdiary.dto.AuthRequestDto;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/create-user-before.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(value = "/create-user-after.sql", executionPhase = AFTER_TEST_METHOD)
public class AuthControllerIntegrationTest {
    @Autowired
    private AuthController authController;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getAuthToken() {
        AuthRequestDto request = new AuthRequestDto();
        request.setEmail("test@myemail.com");
        request.setPassword("Password!1");

        ResponseEntity response = authController.getAuthToken(request);

        assertThat(response.getStatusCode(), equalTo(OK));
    }

    @Test
    public void getAuthTokenWithInvalidEmail() {
        AuthRequestDto request = new AuthRequestDto();
        request.setEmail("invalidEmail@myemail.com");
        request.setPassword("Password!1");

        exceptionRule.expect(BadCredentialsException.class);
        exceptionRule.expectMessage("Invalid email or password");
        authController.getAuthToken(request);
    }

    @Test
    public void getAuthTokenWithInvalidPassword() {
        AuthRequestDto request = new AuthRequestDto();
        request.setEmail("test@myemail.com");
        request.setPassword("InvalidPassword!1");

        exceptionRule.expect(BadCredentialsException.class);
        exceptionRule.expectMessage("Invalid email or password");
        authController.getAuthToken(request);
    }
}