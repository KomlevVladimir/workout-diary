package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.dto.ResetPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.dto.SetupPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.infra.email.EmailMessage;
import com.vladimirkomlev.workoutdiary.infra.messaging.MessageQueues;
import com.vladimirkomlev.workoutdiary.model.ConfirmationSecret;
import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.repository.ConfirmationSecretRepository;
import com.vladimirkomlev.workoutdiary.repository.UserRepository;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserRepository userRepository = mock(UserRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
    private ConfirmationSecretRepository confirmationSecretRepository = mock(ConfirmationSecretRepository.class);
    private SecretLinkService secretLinkService = mock(SecretLinkService.class);
    private MessageQueues messageQueues = mock(MessageQueues.class);
    private UserServiceImpl userService = new UserServiceImpl(
            userRepository,
            passwordEncoder,
            messageQueues,
            confirmationSecretRepository,
            secretLinkService
    );

    @Test
    public void register() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "test@myemail.com";
        User mockUser = new User();
        mockUser.setFirstName(firstName);
        mockUser.setLastName(lastName);
        mockUser.setEmail(email);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(mockUser);
        User newUser = new User();
        newUser.setPassword("Password!1");
        User user = userService.register(newUser);

        assertThat(user.getFirstName(), equalTo(firstName));
        assertThat(user.getLastName(), equalTo(lastName));
        assertThat(user.getEmail(), equalTo(email));
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    public void verifyEmail() {
        String email = "test@myemail.com";
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(new User());
        User user = new User();
        user.setEmail(email);
        userService.verifyEmail(user);

        verify(confirmationSecretRepository, times(1)).save(any(ConfirmationSecret.class));
        verify(messageQueues, times(1)).enqueueEmail(any(EmailMessage.class));
    }

    @Test
    public void confirm() {
        String secret = "secret";
        String email = "test@myemail.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        ConfirmationSecret mockConfirmationSecret = new ConfirmationSecret();
        mockConfirmationSecret.setUser(mockUser);
        when(confirmationSecretRepository.findBySecret(secret)).thenReturn(mockConfirmationSecret);
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        User user = userService.confirm(secret);

        assertThat(user.getEmail(), equalTo(email));
        assertTrue(user.isEnabled());
        verify(confirmationSecretRepository, times(1)).delete(any(ConfirmationSecret.class));
    }

    @Test
    public void resetPassword() {
        String email = "test@myemail.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(mockUser);
        ResetPasswordRequestDto request = new ResetPasswordRequestDto();
        request.setEmail(email);
        userService.resetPassword(request);

        verify(confirmationSecretRepository, times(1)).save(any(ConfirmationSecret.class));
        verify(secretLinkService, times(1)).generatePasswordLink(anyString());
        verify(messageQueues, times(1)).enqueueEmail(any(EmailMessage.class));
    }

    @Test
    public void setupPassword() {
        String secret = "secret";
        String password = "Password!1";
        String email = "test@myemail.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        ConfirmationSecret mockConfirmationSecret = new ConfirmationSecret();
        mockConfirmationSecret.setUser(mockUser);
        when(confirmationSecretRepository.findBySecret(secret)).thenReturn(mockConfirmationSecret);
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(mockUser);
        SetupPasswordRequestDto request = new SetupPasswordRequestDto();
        request.setSecret(secret);
        request.setPassword(password);
        userService.setupPassword(request);

        verify(passwordEncoder, times(1)).encode(password);
        verify(confirmationSecretRepository, times(1)).delete(any(ConfirmationSecret.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void getUserByEmail() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "test@myemail.com";
        User mockUser = new User();
        mockUser.setFirstName(firstName);
        mockUser.setLastName(lastName);
        mockUser.setEmail(email);
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(mockUser);
        User user = userService.getUserByEmail(email);

        assertThat(user.getEmail(), equalTo(email));
        assertThat(user.getFirstName(), equalTo(firstName));
        assertThat(user.getLastName(), equalTo(lastName));
    }
}