package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.dto.ResetPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.dto.SetupPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.exception.NotFoundException;
import com.vladimirkomlev.workoutdiary.infra.email.EmailMessage;
import com.vladimirkomlev.workoutdiary.infra.messaging.MessageQueues;
import com.vladimirkomlev.workoutdiary.model.ConfirmationSecret;
import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.repository.ConfirmationSecretRepository;
import com.vladimirkomlev.workoutdiary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageQueues messageQueues;
    private final ConfirmationSecretRepository confirmationSecretRepository;
    private final SecretLinkService secretLinkService;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            MessageQueues messageQueues,
            ConfirmationSecretRepository confirmationSecretRepository,
            SecretLinkService secretLinkService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageQueues = messageQueues;
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
            messageQueues.enqueueEmail(message);
        } else {
            throw new NotFoundException("User is not created");
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
            throw new NotFoundException("Secret not found");
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
            messageQueues.enqueueEmail(message);
        } else {
            throw new NotFoundException("User not found");
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
            throw new NotFoundException("Secret not found");
        }

    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        return user;
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        return getUserByEmail(email);
    }
}