package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.dto.ResetPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.dto.SetupPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    void verifyEmail(User user);

    User confirm(String code);

    void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);

    void setupPassword(SetupPasswordRequestDto setupPasswordRequestDto);

    User getUserByEmail(String email);

    User getCurrentUser();
}
