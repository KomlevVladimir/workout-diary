package com.vladimirkomlev.workoutdiary.repository;

import com.vladimirkomlev.workoutdiary.model.ConfirmationCode;
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
public class ConfirmationCodeRepositoryIntegrationTest {
    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByCode() {
        User user = new User();
        user.setEmail("test@myemail.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("Password!1");
        entityManager.persist(user);
        ConfirmationCode code = new ConfirmationCode();
        code.setCode("code");
        code.setUser(user);
        ConfirmationCode savedCode = entityManager.persist(code);

        ConfirmationCode foundCode = confirmationCodeRepository.findByCode("code");

        assertNotNull(foundCode);
        Assert.assertThat(foundCode, equalTo(savedCode));
    }
}