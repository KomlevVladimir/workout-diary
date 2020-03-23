package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(value = "/create-user-before.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(value = "/create-user-after.sql", executionPhase = AFTER_TEST_METHOD)
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Test
    public void register() {
        User user = new User();
        user.setFirstName("Jenny");
        user.setLastName("Johnson");
        user.setPassword("Password!1");
        user.setAge(24);
        user.setEmail("jenny@myemail.com");
        User registeredUser = userService.register(user);

        assertNotNull(registeredUser);
        assertNotNull(registeredUser.getId());
        assertThat(registeredUser, equalTo(user));
    }

    @Test
    @Sql(value = "/create-confirmation-code-before.sql", executionPhase = BEFORE_TEST_METHOD)
    public void confirm() {
        User user = userService.confirm("code");

        assertNotNull(user);
        assertTrue(user.isEnabled());
    }

    @Test
    public void getUserByEmail() {
        User user = userService.getUserByEmail("test@myemail.com");

        assertNotNull(user);
        assertThat(user.getId(), equalTo(25L));
        assertThat(user.getFirstName(), equalTo("John"));
        assertThat(user.getLastName(), equalTo("Doe"));
        assertThat(user.getAge(), equalTo(23));
    }

    @Test
    @WithUserDetails("test@myemail.com")
    public void getCurrentUser() {
        User user = userService.getCurrentUser();

        assertNotNull(user);
        assertThat(user.getId(), equalTo(25L));
        assertThat(user.getFirstName(), equalTo("John"));
        assertThat(user.getLastName(), equalTo("Doe"));
        assertThat(user.getAge(), equalTo(23));
    }
}