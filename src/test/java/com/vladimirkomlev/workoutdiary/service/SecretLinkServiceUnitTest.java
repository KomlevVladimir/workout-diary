package com.vladimirkomlev.workoutdiary.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.*;

public class SecretLinkServiceUnitTest {
    private static SecretLinkServiceImpl secretLinkService = new SecretLinkServiceImpl();

    @BeforeClass
    public static void setUp() {
        ReflectionTestUtils.setField(secretLinkService, "baseUrl", "http://127.0.0.1:8080/");
    }

    @Test
    public void generateLink() {
        String path = "/path/";
        String secret = randomUUID().toString();
        String link = secretLinkService.generateLink(path, secret);
        assertTrue(link.contains(path));
        assertTrue(link.contains(secret));
    }

    @Test
    public void generateConfirmationEmailLink() {
        String secret = "secret";
        String confirmationLink = secretLinkService.generateConfirmationEmailLink(secret);
        assertTrue(confirmationLink.contains("/confirm?secret=" + secret));
    }

    @Test
    public void generatePasswordLink() {
        String secret = "secret";
        String passwordLink = secretLinkService.generatePasswordLink(secret);
        assertTrue(passwordLink.contains("/reset?secret=" + secret));
    }
}