package com.vladimirkomlev.workoutdiary.dto;

import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.validation.Age;
import com.vladimirkomlev.workoutdiary.validation.EmailField;
import com.vladimirkomlev.workoutdiary.validation.NotBlankField;
import com.vladimirkomlev.workoutdiary.validation.Password;

import javax.validation.constraints.Email;

public class UserRequestDto {
    @NotBlankField
    private String firstName;
    @NotBlankField
    private String lastName;
    @Age
    private Integer age;
    @EmailField
    private String email;
    @Password
    private String password;

    public static User toUser(UserRequestDto userRequestDto) {
        User user = new User();
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setAge(userRequestDto.getAge());
        user.setEmail(userRequestDto.getEmail().toLowerCase());
        user.setPassword(userRequestDto.getPassword());
        return user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
