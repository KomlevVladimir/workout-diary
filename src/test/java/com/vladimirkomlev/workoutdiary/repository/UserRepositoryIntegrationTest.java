package com.vladimirkomlev.workoutdiary.repository;

import com.vladimirkomlev.workoutdiary.model.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmailIgnoreCase() {
        User user = new User();
        user.setEmail("test@myemail.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("Password!1");
        User savedUserInDb = entityManager.persist(user);

        User foundUser = userRepository.findByEmailIgnoreCase("test@myemail.com");

        assertNotNull(foundUser);
        assertThat(foundUser, equalTo(savedUserInDb));
    }
}