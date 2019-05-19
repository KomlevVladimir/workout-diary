package com.vladimirkomlev.workoutdiary.dto;

import com.vladimirkomlev.workoutdiary.validation.NotBlankField;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class WorkoutCreateUpdateRequestDto {
    @NotNull
    private LocalDate date;
    @NotBlankField
    private String description;

    public WorkoutCreateUpdateRequestDto(@NotNull LocalDate date, String description) {
        this.date = date;
        this.description = description;
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
