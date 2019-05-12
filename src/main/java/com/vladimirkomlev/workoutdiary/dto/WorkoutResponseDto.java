package com.vladimirkomlev.workoutdiary.dto;

import com.vladimirkomlev.workoutdiary.model.Workout;

import java.time.LocalDate;

public class WorkoutResponseDto {
    private long id;
    private LocalDate date;
    private String description;

    public static WorkoutResponseDto toWorkoutResponseDto(Workout workout) {
        WorkoutResponseDto response = new WorkoutResponseDto();
        response.setId(workout.getId());
        response.setDate(workout.getDate());
        response.setDescription(workout.getDescription());
        return response;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
