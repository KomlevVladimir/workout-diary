package com.vladimirkomlev.workoutdiary.controller;

import com.vladimirkomlev.workoutdiary.dto.WorkoutCreateUpdateRequestDto;
import com.vladimirkomlev.workoutdiary.dto.WorkoutResponseDto;
import com.vladimirkomlev.workoutdiary.model.Workout;
import com.vladimirkomlev.workoutdiary.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/")
public class WorkoutController {
    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping(value = "/users/{userId}/workouts")
    public ResponseEntity createWorkout(
            @PathVariable Long userId,
            @Valid @RequestBody WorkoutCreateUpdateRequestDto workoutCreateUpdateRequestDto
    ) {
        Workout workout = workoutService.create(workoutCreateUpdateRequestDto, userId);
        WorkoutResponseDto response = WorkoutResponseDto.toWorkoutResponseDto(workout);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/users/{userId}/workouts")
    public ResponseEntity getAllWorkouts(@PathVariable Long userId) {
        List<WorkoutResponseDto> response = workoutService.getAllWorkoutsByUserId(userId)
                .stream()
                .map(WorkoutResponseDto::toWorkoutResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/users/{userId}/workouts/{workoutId}")
    public ResponseEntity getWorkout(@PathVariable Long userId, @PathVariable Long workoutId) {
        Workout workout = workoutService.get(userId, workoutId);
        WorkoutResponseDto response = WorkoutResponseDto.toWorkoutResponseDto(workout);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/users/{userId}/workouts/{workoutId}")
    public ResponseEntity updateWorkout(@Valid @RequestBody WorkoutCreateUpdateRequestDto workoutCreateUpdateRequestDto, @PathVariable Long userId, @PathVariable Long workoutId) {
        Workout workout = workoutService.update(workoutCreateUpdateRequestDto, userId, workoutId);
        WorkoutResponseDto response = WorkoutResponseDto.toWorkoutResponseDto(workout);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/users/{userId}/workouts/{workoutId}")
    public ResponseEntity deleteWorkout(@PathVariable Long userId, @PathVariable Long workoutId) {
        workoutService.delete(userId, workoutId);
        return ResponseEntity.ok(null);
    }
}
