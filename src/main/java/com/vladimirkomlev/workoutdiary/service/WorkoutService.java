package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.dto.WorkoutCreateUpdateRequestDto;
import com.vladimirkomlev.workoutdiary.model.Workout;

import java.util.List;

public interface WorkoutService {
    List<Workout> getAllWorkoutsByUserId(Long userId);

    Workout get(Long userId, Long workoutId);

    Workout create(WorkoutCreateUpdateRequestDto workoutCreateUpdateRequestDto, Long userId);

    Workout update(WorkoutCreateUpdateRequestDto workoutCreateUpdateRequestDto, Long userId, Long workoutId);

    void delete(Long userId, Long workoutId);

}
