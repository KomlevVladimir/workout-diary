package com.vladimirkomlev.workoutdiary.dto;

import com.vladimirkomlev.workoutdiary.validation.NotBlankField;
import com.vladimirkomlev.workoutdiary.validation.Password;

public class SetupPasswordRequestDto {
    @NotBlankField
    private String code;
    @Password
    private String password;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
