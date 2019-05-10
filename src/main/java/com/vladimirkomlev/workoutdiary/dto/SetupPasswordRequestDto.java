package com.vladimirkomlev.workoutdiary.dto;

import com.vladimirkomlev.workoutdiary.validation.NotBlankField;
import com.vladimirkomlev.workoutdiary.validation.Password;

public class SetupPasswordRequestDto {
    @NotBlankField
    private String secret;
    @NotBlankField
    @Password
    private String password;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
