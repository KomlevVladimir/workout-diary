package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.dto.ResetPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.dto.SetupPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.infra.email.EmailMessage;
import com.vladimirkomlev.workoutdiary.infra.messaging.MessageQueues;
import com.vladimirkomlev.workoutdiary.model.ConfirmationCode;
import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.repository.ConfirmationCodeRepository;
import com.vladimirkomlev.workoutdiary.repository.UserRepository;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceUnitTest {
    private UserRepository userRepository = mock(UserRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
    private ConfirmationCodeRepository confirmationCodeRepository = mock(ConfirmationCodeRepository.class);
    private MessageQueues messageQueues = mock(MessageQueues.class);
    private UserServiceImpl userService = new UserServiceImpl(
            userRepository,
            passwordEncoder,
            messageQueues,
            confirmationCodeRepository
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

        verify(confirmationCodeRepository, times(1)).save(any(ConfirmationCode.class));
        verify(messageQueues, times(1)).enqueueEmail(any(EmailMessage.class));
    }

    @Test
    public void confirm() {
        String code = "code";
        String email = "test@myemail.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        ConfirmationCode mockConfirmationCode = new ConfirmationCode();
        mockConfirmationCode.setUser(mockUser);
        when(confirmationCodeRepository.findByCode(code)).thenReturn(mockConfirmationCode);
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        User user = userService.confirm(code);

        assertThat(user.getEmail(), equalTo(email));
        assertTrue(user.isEnabled());
        verify(confirmationCodeRepository, times(1)).delete(any(ConfirmationCode.class));
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

        verify(confirmationCodeRepository, times(1)).save(any(ConfirmationCode.class));
        verify(messageQueues, times(1)).enqueueEmail(any(EmailMessage.class));
    }

    @Test
    public void setupPassword() {
        String code = "code";
        String password = "Password!1";
        String email = "test@myemail.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        ConfirmationCode mockConfirmationCode = new ConfirmationCode();
        mockConfirmationCode.setUser(mockUser);
        when(confirmationCodeRepository.findByCode(code)).thenReturn(mockConfirmationCode);
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(mockUser);
        SetupPasswordRequestDto request = new SetupPasswordRequestDto();
        request.setCode(code);
        request.setPassword(password);
        userService.setupPassword(request);

        verify(passwordEncoder, times(1)).encode(password);
        verify(confirmationCodeRepository, times(1)).delete(any(ConfirmationCode.class));
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