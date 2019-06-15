package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.dto.WorkoutCreateUpdateRequestDto;
import com.vladimirkomlev.workoutdiary.exception.NotFoundException;
import com.vladimirkomlev.workoutdiary.model.Workout;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(value = {"/create-user-before.sql", "/create-workout-before.sql"}, executionPhase = BEFORE_TEST_METHOD)
@Sql(value = {"/create-workout-after.sql", "/create-user-after.sql"}, executionPhase = AFTER_TEST_METHOD)
public class WorkoutServiceIntegrationTest {
    @Autowired
    private WorkoutService workoutService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    @WithUserDetails("test@myemail.com")
    public void getAllWorkoutsByUserId() {
        List<Workout> workouts = workoutService.getAllWorkoutsByUserId(25L);

        assertThat(workouts.size(), equalTo(2));
        assertThat(workouts.get(0).getDate(), equalTo(LocalDate.parse("2018-07-01")));
        assertThat(workouts.get(0).getDescription(), equalTo("Cycling 30 miles"));
        assertThat(workouts.get(1).getDate(), equalTo(LocalDate.parse("2018-08-01")));
        assertThat(workouts.get(1).getDescription(), equalTo("Running 5 miles"));
    }

    @Test
    @WithUserDetails("test@myemail.com")
    public void getWorkout() {
        Workout workout = workoutService.get(25L, 10L);

        assertNotNull(workout);
        assertThat(workout.getId(), greaterThan(0L));
        assertThat(workout.getDate(), equalTo(LocalDate.parse("2018-07-01")));
        assertThat(workout.getDescription(), equalTo("Cycling 30 miles"));
    }

    @Test
    @WithUserDetails("test@myemail.com")
    public void createWorkout() {
        LocalDate date = LocalDate.parse("2014-05-22");
        String description = "Running 5 miles";
        WorkoutCreateUpdateRequestDto request = new WorkoutCreateUpdateRequestDto(date, description);

        Workout workout = workoutService.create(request, 25L);

        assertNotNull(workout);
        assertThat(workout.getId(), greaterThan(0L));
        assertThat(workout.getDate(), equalTo(date));
        assertThat(workout.getDescription(), equalTo(description));
    }

    @Test
    @WithUserDetails("test@myemail.com")
    public void updateWorkout() {
        LocalDate date = LocalDate.parse("2014-05-22");
        String description = "Playing football";
        WorkoutCreateUpdateRequestDto request = new WorkoutCreateUpdateRequestDto(date, description);

        Workout workout = workoutService.update(request, 25L, 10L);

        assertThat(workout.getDate(), equalTo(date));
        assertThat(workout.getDescription(), equalTo(description));
        assertThat(workout.getId(), equalTo(10L));
    }

    @Test
    @WithUserDetails("test@myemail.com")
    public void delete() {
        workoutService.delete(25L, 10L);

        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("Workout not found");
        workoutService.delete(25L, 10L);
    }
}