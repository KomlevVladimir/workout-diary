package com.vladimirkomlev.workoutdiary.dto;

import com.vladimirkomlev.workoutdiary.validation.EmailField;
import com.vladimirkomlev.workoutdiary.validation.NotBlankField;

public class ResetPasswordRequestDto {
    @EmailField
    @NotBlankField
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
