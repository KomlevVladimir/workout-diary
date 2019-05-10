package com.vladimirkomlev.workoutdiary.service;

public interface SecretLinkService {
    String generateLink(String path, String secret);

    String generateConfirmationEmailLink(String secret);
}
