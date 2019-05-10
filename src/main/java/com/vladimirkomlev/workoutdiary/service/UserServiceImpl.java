package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.infra.email.EmailMessage;
import com.vladimirkomlev.workoutdiary.infra.email.EmailSender;
import com.vladimirkomlev.workoutdiary.model.ConfirmationSecret;
import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.repository.ConfirmationSecretRepository;
import com.vladimirkomlev.workoutdiary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;
import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final ConfirmationSecretRepository confirmationSecretRepository;
    private final SecretLinkService secretLinkService;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            EmailSender emailSender,
            ConfirmationSecretRepository confirmationSecretRepository,
            SecretLinkService secretLinkService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
        this.confirmationSecretRepository = confirmationSecretRepository;
        this.secretLinkService = secretLinkService;
    }

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);
        verifyEmail(createdUser);
        return createdUser;
    }

    @Override
    public void verifyEmail(User user) {
        User createdUser = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (createdUser != null) {
            ConfirmationSecret confirmationSecret = new ConfirmationSecret(user);
            confirmationSecretRepository.save(confirmationSecret);
            EmailMessage message = new EmailMessage();
            message.setRecipient(user.getEmail());
            message.setSubject("Confirm your account");
            String link = secretLinkService.generateConfirmationEmailLink(confirmationSecret.getSecret());
            message.setMessage(link);
            emailSender.sendEmail(message);
        } else {
            throw new IllegalStateException("User is not created");
        }
    }

    @Override
    public User confirm(String secret) {
        ConfirmationSecret confirmationSecret = confirmationSecretRepository.findBySecret(secret);
        if (confirmationSecret != null) {
            User user = userRepository.findByEmailIgnoreCase(confirmationSecret.getUser().getEmail());
            user.setEnabled(true);
            confirmationSecretRepository.delete(confirmationSecret);
            return userRepository.save(user);
        } else {
            throw new IllegalStateException("Secret not found");
        }
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}