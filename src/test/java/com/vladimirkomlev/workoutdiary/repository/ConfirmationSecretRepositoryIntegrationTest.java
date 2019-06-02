package com.vladimirkomlev.workoutdiary.repository;

import com.vladimirkomlev.workoutdiary.model.ConfirmationSecret;
import com.vladimirkomlev.workoutdiary.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConfirmationSecretRepositoryIntegrationTest {
    @Autowired
    private ConfirmationSecretRepository confirmationSecretRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findBySecret() {
        User user = new User();
        user.setEmail("test@myemail.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("Password!1");
        entityManager.persist(user);
        ConfirmationSecret secret = new ConfirmationSecret();
        secret.setSecret("secret");
        secret.setUser(user);
        ConfirmationSecret savedSecret = entityManager.persist(secret);

        ConfirmationSecret foundSecret = confirmationSecretRepository.findBySecret("secret");

        assertNotNull(foundSecret);
        Assert.assertThat(foundSecret, equalTo(savedSecret));
    }
}