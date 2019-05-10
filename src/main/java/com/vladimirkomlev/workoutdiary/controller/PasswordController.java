package com.vladimirkomlev.workoutdiary.controller;

import com.vladimirkomlev.workoutdiary.dto.ResetPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.dto.SetupPasswordRequestDto;
import com.vladimirkomlev.workoutdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/")
public class PasswordController {
    private UserService userService;

    @Autowired
    public  PasswordController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "reset-password")
    public ResponseEntity resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        userService.resetPassword(resetPasswordRequestDto);
        return ResponseEntity.ok(null);
    }

    @GetMapping(value = "reset")
    public ResponseEntity reset(@RequestParam String secret) {
        //TODO return page with forms password and predefined secret
        return ResponseEntity.ok(null);
    }

    @PostMapping(value = "setup-password")
    public ResponseEntity setupPassword(@Valid @RequestBody SetupPasswordRequestDto setupPasswordRequestDto) {
        userService.setupPassword(setupPasswordRequestDto);
        return ResponseEntity.ok(null);
    }
}
