package com.vladimirkomlev.workoutdiary.controller;

import com.vladimirkomlev.workoutdiary.dto.UserRequestDto;
import com.vladimirkomlev.workoutdiary.dto.UserResponseDto;
import com.vladimirkomlev.workoutdiary.exception.NotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/create-user-before.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(value = "/create-user-after.sql", executionPhase = AFTER_TEST_METHOD)
public class RegistrationControllerIntegrationTest {
    @Autowired
    RegistrationController registrationController;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void signUp() {
        UserRequestDto request = new UserRequestDto();
        request.setFirstName("Jenny");
        request.setLastName("Johnson");
        request.setAge(24);
        request.setEmail("jenny@myemail.com");
        request.setPassword("Password!1");

        ResponseEntity response = registrationController.signUp(request);
        UserResponseDto responseBody = (UserResponseDto) response.getBody();

        assertThat(response.getStatusCode(), equalTo(OK));
        assertThat(requireNonNull(responseBody).getId(), greaterThan(0L));
        assertThat(responseBody.getFirstName(), equalTo(request.getFirstName()));
        assertThat(responseBody.getLastName(), equalTo(request.getLastName()));
        assertThat(responseBody.getAge(), equalTo(request.getAge()));
        assertThat(responseBody.getEmail(), equalTo(request.getEmail()));
    }

    @Test
    public void signUpUserWithAlreadyExistedEmail() {
        UserRequestDto request = new UserRequestDto();
        request.setFirstName("Ivan");
        request.setLastName("Ivanov");
        request.setAge(29);
        request.setEmail("test@myemail.com");
        request.setPassword("Password!1");

        exceptionRule.expect(DataIntegrityViolationException.class);
        registrationController.signUp(request);
    }

    @Test
    @Sql(value = "/create-confirmation-secret-before.sql", executionPhase = BEFORE_TEST_METHOD)
    public void confirm() {
        ResponseEntity response = registrationController.confirm("secret");
        UserResponseDto responseBody = (UserResponseDto) response.getBody();

        assertThat(response.getStatusCode(), equalTo(OK));
        assertThat(requireNonNull(responseBody).getId(), equalTo(13L));
        assertThat(responseBody.getFirstName(), equalTo("David"));
        assertThat(responseBody.getLastName(), equalTo("Laurie"));
        assertThat(responseBody.getAge(), equalTo(37));
        assertThat(responseBody.getEmail(), equalTo("david@myemail.com"));
    }

    @Test
    public void confirmWithNonExistentSecret() {
        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("Secret not found");
        registrationController.confirm("qwerty");
    }
}