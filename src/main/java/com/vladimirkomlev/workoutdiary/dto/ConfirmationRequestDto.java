package com.vladimirkomlev.workoutdiary.dto;

import com.vladimirkomlev.workoutdiary.validation.NotBlankField;

public class ConfirmationRequestDto {
    @NotBlankField
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
