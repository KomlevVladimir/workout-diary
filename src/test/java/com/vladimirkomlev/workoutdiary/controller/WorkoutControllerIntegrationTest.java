package com.vladimirkomlev.workoutdiary.controller;

import com.vladimirkomlev.workoutdiary.dto.WorkoutCreateUpdateRequestDto;
import com.vladimirkomlev.workoutdiary.dto.WorkoutResponseDto;
import com.vladimirkomlev.workoutdiary.exception.NotFoundException;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = {"/create-user-before.sql", "/create-workout-before.sql"}, executionPhase = BEFORE_TEST_METHOD)
@Sql(value = {"/create-workout-after.sql", "/create-user-after.sql"}, executionPhase = AFTER_TEST_METHOD)
@WithUserDetails("test@myemail.com")
public class WorkoutControllerIntegrationTest {

    @Autowired
    WorkoutController workoutController;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void createWorkout() {
        WorkoutCreateUpdateRequestDto request =
                new WorkoutCreateUpdateRequestDto(LocalDate.parse("2014-05-22"), "Swimming 2 miles", "Swimming");

        ResponseEntity response = workoutController.createWorkout(25L, request);
        WorkoutResponseDto responseBody = (WorkoutResponseDto) response.getBody();

        assertThat(response.getStatusCode(), equalTo(OK));
        assertThat(requireNonNull(responseBody).getId(), greaterThan(0L));
        assertThat(responseBody.getDate(), equalTo(request.getDate()));
        assertThat(responseBody.getDescription(), equalTo(request.getDescription()));
        assertThat(responseBody.getTitle(), equalTo(request.getTitle()));
    }

    @Test
    public void createWorkoutByNotCurrentUser() {
        WorkoutCreateUpdateRequestDto request =
                new WorkoutCreateUpdateRequestDto(LocalDate.parse("2014-05-22"), "Swimming 2 miles", "Swimming");

        exceptionRule.expect(AccessDeniedException.class);
        exceptionRule.expectMessage("Access denied");
        workoutController.createWorkout(100L, request);
    }

    @Test
    public void getAllWorkouts() {
        ResponseEntity response = workoutController.getAllWorkouts(25L);
        List<WorkoutResponseDto> workouts = (List<WorkoutResponseDto>) response.getBody();

        assertThat(response.getStatusCode(), equalTo(OK));
        assertThat(requireNonNull(workouts).size(), Matchers.equalTo(2));
        assertThat(workouts.get(0).getDate(), Matchers.equalTo(LocalDate.parse("2018-08-01")));
        assertThat(workouts.get(0).getDescription(), Matchers.equalTo("Running 5 miles"));
        assertThat(workouts.get(0).getTitle(), Matchers.equalTo("Morning running"));
        assertThat(workouts.get(1).getDate(), Matchers.equalTo(LocalDate.parse("2018-07-01")));
        assertThat(workouts.get(1).getDescription(), Matchers.equalTo("Cycling 30 miles"));
        assertThat(workouts.get(1).getTitle(), Matchers.equalTo("Cycling"));
    }

    @Test
    public void getAllWorkoutsByNotCurrentUser() {
        exceptionRule.expect(AccessDeniedException.class);
        exceptionRule.expectMessage("Access denied");
        workoutController.getAllWorkouts(100L);
    }

    @Test
    public void getWorkout() {
        ResponseEntity response = workoutController.getWorkout(25L, 10L);
        WorkoutResponseDto responseBody = (WorkoutResponseDto) response.getBody();

        assertThat(response.getStatusCode(), equalTo(OK));
        assertThat(requireNonNull(responseBody).getDate(), Matchers.equalTo(LocalDate.parse("2018-07-01")));
        assertThat(responseBody.getDescription(), Matchers.equalTo("Cycling 30 miles"));
    }

    @Test
    public void getWorkoutByNotCurrentUser() {
        exceptionRule.expect(AccessDeniedException.class);
        exceptionRule.expectMessage("Access denied");
        workoutController.getWorkout(100L, 10L);
    }

    @Test
    public void getNonExistentWorkout() {
        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("Workout not found");
        workoutController.getWorkout(25L, 25L);
    }

    @Test
    public void updateWorkout() {
        WorkoutCreateUpdateRequestDto request =
                new WorkoutCreateUpdateRequestDto(LocalDate.parse("2014-05-22"), "Swimming 2 miles", "Swimming");

        ResponseEntity response = workoutController.updateWorkout(request, 25L, 10L);
        WorkoutResponseDto responseBody = (WorkoutResponseDto) response.getBody();

        assertThat(response.getStatusCode(), equalTo(OK));
        assertThat(requireNonNull(responseBody).getId(), equalTo(10L));
        assertThat(responseBody.getDate(), equalTo(request.getDate()));
        assertThat(responseBody.getDescription(), equalTo(request.getDescription()));
        assertThat(responseBody.getTitle(), equalTo(request.getTitle()));
    }

    @Test
    public void updateWorkoutByNotCurrentUser() {
        WorkoutCreateUpdateRequestDto request =
                new WorkoutCreateUpdateRequestDto(LocalDate.parse("2014-05-22"), "Swimming 2 miles", "Morning swimming");

        exceptionRule.expect(AccessDeniedException.class);
        exceptionRule.expectMessage("Access denied");
        workoutController.updateWorkout(request, 100L, 10L);
    }

    @Test
    public void updateNonExistentWorkout() {
        WorkoutCreateUpdateRequestDto request =
                new WorkoutCreateUpdateRequestDto(LocalDate.parse("2014-05-22"), "Swimming 2 miles", "Swimming");

        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("Workout not found");
        workoutController.updateWorkout(request, 25L, 100L);
    }

    @Test
    public void deleteWorkout() {
        ResponseEntity response = workoutController.deleteWorkout(25L, 10L);

        assertThat(response.getStatusCode(), equalTo(OK));
        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("Workout not found");
        workoutController.deleteWorkout(25L, 10L);
    }

    @Test
    public void deleteWorkoutByNotCurrentUser() {
        exceptionRule.expect(AccessDeniedException.class);
        exceptionRule.expectMessage("Access denied");
        workoutController.deleteWorkout(100L, 10L);
    }
}