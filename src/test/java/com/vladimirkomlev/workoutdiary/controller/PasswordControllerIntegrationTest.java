package com.vladimirkomlev.workoutdiary.controller;

import com.vladimirkomlev.workoutdiary.dto.ResetPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.dto.SetupPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.exception.NotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
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
public class PasswordControllerIntegrationTest {
    @Autowired
    private PasswordController passwordController;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void resetPassword() {
        ResetPasswordRequestDto request = new ResetPasswordRequestDto();
        request.setEmail("test@myemail.com");

        ResponseEntity response = passwordController.resetPassword(request);

        assertThat(response.getStatusCode(), equalTo(OK));
    }

    @Test
    public void resetPasswordOfUserWithNonExistentEmail() {
        ResetPasswordRequestDto request = new ResetPasswordRequestDto();
        request.setEmail("nonexistentuser@myemail.com");

        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("User not found");
        passwordController.resetPassword(request);
    }

    @Test
    @Sql(value = "/create-confirmation-secret-before.sql", executionPhase = BEFORE_TEST_METHOD)
    public void setupPassword() {
        SetupPasswordRequestDto request = new SetupPasswordRequestDto();
        request.setSecret("secret");
        request.setPassword("NewPassword!1");

        ResponseEntity response = passwordController.setupPassword(request);

        assertThat(response.getStatusCode(), equalTo(OK));
    }

    @Test
    @Sql(value = "/create-confirmation-secret-before.sql", executionPhase = BEFORE_TEST_METHOD)
    public void setupPasswordWithNonExistentSecret() {
        SetupPasswordRequestDto request = new SetupPasswordRequestDto();
        request.setSecret("12345");
        request.setPassword("Password!1");

        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("Secret not found");
        passwordController.setupPassword(request);
    }
}