package com.vladimirkomlev.workoutdiary.repository;

import com.vladimirkomlev.workoutdiary.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

}
