package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.dto.WorkoutCreateUpdateRequestDto;
import com.vladimirkomlev.workoutdiary.model.Workout;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class WorkoutServiceIntegrationTest {
    @Autowired
    private WorkoutService workoutService;

//    @Autowired
//    private BCryptPasswordEncoder encoder;

    @Test
    public void getAllWorkoutsByUserId() {
    }

    @Test
    public void get() {
    }

    @Test
    public void create() {
//        System.out.println(encoder.encode("Password!1"));
//        LocalDate date = LocalDate.parse("2014-05-22");
//        String description = "Running 5 miles";
//        WorkoutCreateUpdateRequestDto request = new WorkoutCreateUpdateRequestDto(date, description);
//        Workout workout = workoutService.create(request, currentUserId);

    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }
}