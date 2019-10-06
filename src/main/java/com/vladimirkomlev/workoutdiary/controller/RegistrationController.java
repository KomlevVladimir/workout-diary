package com.vladimirkomlev.workoutdiary.controller;

import com.vladimirkomlev.workoutdiary.dto.ConfirmationRequestDto;
import com.vladimirkomlev.workoutdiary.dto.UserRequestDto;
import com.vladimirkomlev.workoutdiary.dto.UserResponseDto;
import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/")
public class RegistrationController {
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "signup")
    public ResponseEntity signUp(@Valid @RequestBody UserRequestDto userRequestDto) {
        User user = UserRequestDto.toUser(userRequestDto);
        userService.register(user);
        UserResponseDto response = UserResponseDto.toUserResponseDto(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "confirm")
    public ResponseEntity confirm(@Valid @RequestBody ConfirmationRequestDto confirmationRequestDto) {
        User user = userService.confirm(confirmationRequestDto.getSecret());
        UserResponseDto response = UserResponseDto.toUserResponseDto(user);
        return ResponseEntity.ok(response);
    }
}
