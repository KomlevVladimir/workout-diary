package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.dto.ResetPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.dto.SetupPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.exception.NotFoundException;
import com.vladimirkomlev.workoutdiary.infra.email.EmailMessage;
import com.vladimirkomlev.workoutdiary.infra.messaging.MessageQueues;
import com.vladimirkomlev.workoutdiary.model.ConfirmationCode;
import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.repository.ConfirmationCodeRepository;
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
    private final ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            MessageQueues messageQueues,
            ConfirmationCodeRepository confirmationCodeRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageQueues = messageQueues;
        this.confirmationCodeRepository = confirmationCodeRepository;
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
            ConfirmationCode confirmationCode = new ConfirmationCode(user);
            confirmationCodeRepository.save(confirmationCode);
            EmailMessage message = new EmailMessage();
            message.setRecipient(user.getEmail());
            message.setSubject("Confirm your account");
            message.setMessage("Your confirmation code is: " + confirmationCode.getCode());
            messageQueues.enqueueEmail(message);
        } else {
            throw new NotFoundException("User is not created");
        }
    }

    @Override
    public User confirm(String code) {
        ConfirmationCode confirmationCode = confirmationCodeRepository.findByCode(code);
        if (confirmationCode != null) {
            User user = userRepository.findByEmailIgnoreCase(confirmationCode.getUser().getEmail());
            user.setEnabled(true);
            confirmationCodeRepository.delete(confirmationCode);
            return userRepository.save(user);
        } else {
            throw new NotFoundException("Code not found");
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        User user = userRepository.findByEmailIgnoreCase(resetPasswordRequestDto.getEmail());
        if (user != null) {
            ConfirmationCode confirmationCode = new ConfirmationCode(user);
            confirmationCodeRepository.save(confirmationCode);
            EmailMessage message = new EmailMessage();
            message.setRecipient(user.getEmail());
            message.setSubject("Reset password");
            message.setMessage("Your reset password code is: " + confirmationCode.getCode());
            messageQueues.enqueueEmail(message);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public void setupPassword(SetupPasswordRequestDto setupPasswordRequestDto) {
        ConfirmationCode confirmationCode = confirmationCodeRepository.findByCode(setupPasswordRequestDto.getCode());
        if (confirmationCode != null) {
            User user = userRepository.findByEmailIgnoreCase(confirmationCode.getUser().getEmail());
            user.setPassword(passwordEncoder.encode(setupPasswordRequestDto.getPassword()));
            confirmationCodeRepository.delete(confirmationCode);
            userRepository.save(user);
        } else {
            throw new NotFoundException("Code not found");
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