package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.model.ConfirmationSecret;
import com.vladimirkomlev.workoutdiary.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Service
public class SecretLinkServiceImpl implements SecretLinkService {
    @Value("${workout-diary.url}")
    private String baseUrl;

    @Override
    public String generateLink(String path, String secret) {
        return fromHttpUrl(baseUrl)
                .path(path)
                .path(secret)
                .build()
                .toUriString();
    }

    @Override
    public String generateConfirmationEmailLink(String secret) {
        return generateLink("/confirm?secret=", secret);
    }
}
