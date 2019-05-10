package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.dto.ResetPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.dto.SetupPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.infra.email.EmailMessage;
import com.vladimirkomlev.workoutdiary.infra.email.EmailSender;
import com.vladimirkomlev.workoutdiary.model.ConfirmationSecret;
import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.repository.ConfirmationSecretRepository;
import com.vladimirkomlev.workoutdiary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        User user = userRepository.findByEmailIgnoreCase(resetPasswordRequestDto.getEmail());
        if (user != null) {
            ConfirmationSecret confirmationSecret = new ConfirmationSecret(user);
            confirmationSecretRepository.save(confirmationSecret);
            EmailMessage message = new EmailMessage();
            message.setRecipient(user.getEmail());
            message.setSubject("Reset password");
            String link = secretLinkService.generatePasswordLink(confirmationSecret.getSecret());
            message.setMessage(link);
            emailSender.sendEmail(message);
        } else {
            throw new IllegalStateException("User not found");
        }
    }

    @Override
    public void setupPassword(SetupPasswordRequestDto setupPasswordRequestDto) {
        ConfirmationSecret confirmationSecret = confirmationSecretRepository.findBySecret(setupPasswordRequestDto.getSecret());
        if (confirmationSecret != null) {
            User user = userRepository.findByEmailIgnoreCase(confirmationSecret.getUser().getEmail());
            user.setPassword(passwordEncoder.encode(setupPasswordRequestDto.getPassword()));
            confirmationSecretRepository.delete(confirmationSecret);
            userRepository.save(user);
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