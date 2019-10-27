package com.vladimirkomlev.workoutdiary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladimirkomlev.workoutdiary.model.Workout;

import java.time.LocalDate;

public class WorkoutResponseDto {
    private long id;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String description;

    public static WorkoutResponseDto toWorkoutResponseDto(Workout workout) {
        WorkoutResponseDto response = new WorkoutResponseDto();
        response.setId(workout.getId());
        response.setTitle(workout.getTitle());
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
