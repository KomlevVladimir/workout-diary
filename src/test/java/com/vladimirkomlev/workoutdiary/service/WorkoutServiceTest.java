package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.dto.WorkoutCreateUpdateRequestDto;
import com.vladimirkomlev.workoutdiary.exception.NotFoundException;
import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.model.Workout;
import com.vladimirkomlev.workoutdiary.repository.WorkoutRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WorkoutServiceTest {
    private WorkoutRepository workoutRepository = mock(WorkoutRepository.class);
    private UserService userService = mock(UserService.class);
    private WorkoutServiceImpl workoutService = new WorkoutServiceImpl(workoutRepository, userService);

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void createWorkout() {
        LocalDate date = LocalDate.parse("2014-05-22");
        String description = "Running 5 miles";
        long currentUserId = 200L;
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);
        Workout workoutMock = new Workout(0, date, description, currentUserMock);
        when(workoutRepository.save(any(Workout.class))).thenReturn(workoutMock);
        WorkoutCreateUpdateRequestDto request = new WorkoutCreateUpdateRequestDto(date, description);
        Workout workout = workoutService.create(request, currentUserId);

        assertThat(workout.getDate(), equalTo(date));
        assertThat(workout.getDescription(), equalTo(description));
        assertThat(workout.getUser(), equalTo(currentUserMock));
        verify(userService, times(1)).getCurrentUser();
    }

    @Test
    public void createWorkoutForAnotherUser() {
        LocalDate date = LocalDate.parse("2019-03-15");
        String description = "Running 5 miles";
        long currentUserId = 300L;
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);
        Workout workoutMock = new Workout(0, date, description, currentUserMock);
        when(workoutRepository.save(any(Workout.class))).thenReturn(workoutMock);
        WorkoutCreateUpdateRequestDto request = new WorkoutCreateUpdateRequestDto(date, description);

        exceptionRule.expect(AccessDeniedException.class);
        exceptionRule.expectMessage("Access denied");
        workoutService.create(request, 200L);
    }

    @Test
    public void getWorkout() {
        LocalDate date = LocalDate.parse("2019-03-15");
        String description = "Running 5 miles";
        long currentUserId = 300L;
        long workoutId = 289L;
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);
        Workout workoutMock = new Workout(workoutId, date, description, currentUserMock);
        when(workoutRepository.findById(workoutId)).thenReturn(of(workoutMock));
        Workout workout = workoutService.get(currentUserId, workoutId);

        assertThat(workout.getDate(), equalTo(date));
        assertThat(workout.getDescription(), equalTo(description));
        assertThat(workout.getId(), equalTo(workoutId));
        assertThat(workout.getUser(), equalTo(currentUserMock));
        verify(userService, times(1)).getCurrentUser();
    }

    @Test
    public void getWorkoutForAnotherUser() {
        LocalDate date = LocalDate.parse("2019-03-15");
        String description = "Running 5 miles";
        long currentUserId = 300L;
        long workoutId = 289L;
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);
        Workout workoutMock = new Workout(workoutId, date, description, currentUserMock);
        when(workoutRepository.findById(workoutId)).thenReturn(of(workoutMock));

        exceptionRule.expect(AccessDeniedException.class);
        exceptionRule.expectMessage("Access denied");
        workoutService.get(100L, workoutId);

    }

    @Test
    public void getWorkoutThatCurrentUserDidNotCreate() {
        Long currentUserId = 300L;
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);

        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("Workout not found");
        workoutService.get(currentUserId, 230L);
    }

    @Test
    public void updateWorkout() {
        long workoutId = 55L;
        long currentUserId = 200L;
        LocalDate currentDate = LocalDate.parse("2014-05-22");
        String currentDescription = "Running 5 miles";
        LocalDate newDate = LocalDate.parse("2018-01-02");
        String newDescription = "Swimming 1 mile";
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);
        Workout currentWorkoutMock = new Workout(workoutId, currentDate, currentDescription, currentUserMock);
        when(workoutRepository.findById(workoutId)).thenReturn(of(currentWorkoutMock));
        Workout newWorkoutMock = new Workout(workoutId, newDate, newDescription, currentUserMock);
        when(workoutRepository.save(any(Workout.class))).thenReturn(newWorkoutMock);
        WorkoutCreateUpdateRequestDto request = new WorkoutCreateUpdateRequestDto(newDate, newDescription);
        Workout updatedWorkout = workoutService.update(request, currentUserId, workoutId);

        assertThat(updatedWorkout.getDate(), equalTo(newDate));
        assertThat(updatedWorkout.getDescription(), equalTo(newDescription));
        assertThat(updatedWorkout.getId(), equalTo(workoutId));
        assertThat(updatedWorkout.getUser(), equalTo(currentUserMock));
        verify(userService, times(1)).getCurrentUser();
        verify(workoutRepository, times(1)).findById(workoutId);
    }

    @Test
    public void updateWorkoutForAnotherUser() {
        long workoutId = 55L;
        long currentUserId = 200L;
        LocalDate currentDate = LocalDate.parse("2014-05-22");
        String currentDescription = "Running 5 miles";
        LocalDate newDate = LocalDate.parse("2018-01-02");
        String newDescription = "Swimming 1 mile";
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);
        Workout currentWorkoutMock = new Workout(workoutId, currentDate, currentDescription, currentUserMock);
        when(workoutRepository.findById(workoutId)).thenReturn(of(currentWorkoutMock));
        Workout newWorkoutMock = new Workout(workoutId, newDate, newDescription, currentUserMock);
        when(workoutRepository.save(any(Workout.class))).thenReturn(newWorkoutMock);
        WorkoutCreateUpdateRequestDto request = new WorkoutCreateUpdateRequestDto(newDate, newDescription);

        exceptionRule.expect(AccessDeniedException.class);
        exceptionRule.expectMessage("Access denied");
        workoutService.update(request, 120L, workoutId);
    }

    @Test
    public void updateWorkoutThatCurrentUserDidNotCreate() {
        long currentUserId = 200L;
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);
        WorkoutCreateUpdateRequestDto request =
                new WorkoutCreateUpdateRequestDto(LocalDate.parse("2018-01-02"), "Swimming 1 mile");

        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("Workout not found");
        workoutService.update(request, currentUserId, 1120L);
    }

    @Test
    public void deleteWorkout() {
        LocalDate date = LocalDate.parse("2019-03-15");
        String description = "Running 5 miles";
        long currentUserId = 300L;
        long workoutId = 289L;
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);
        Workout workoutMock = new Workout(workoutId, date, description, currentUserMock);
        when(workoutRepository.findById(workoutId)).thenReturn(of(workoutMock));
        workoutService.delete(currentUserId, workoutId);

        verify(userService, times(1)).getCurrentUser();
        verify(workoutRepository, times(1)).findById(workoutId);
    }

    @Test
    public void deleteWorkoutForAnotherUser() {
        LocalDate date = LocalDate.parse("2019-03-15");
        String description = "Running 5 miles";
        long currentUserId = 300L;
        long workoutId = 289L;
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);
        Workout workoutMock = new Workout(workoutId, date, description, currentUserMock);
        when(workoutRepository.findById(workoutId)).thenReturn(of(workoutMock));

        exceptionRule.expect(AccessDeniedException.class);
        exceptionRule.expectMessage("Access denied");
        workoutService.delete(15L, workoutId);
    }

    @Test
    public void deleteWorkoutThatCurrentUserDidNotCreate() {
        long currentUserId = 300L;
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);

        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("Workout not found");
        workoutService.delete(currentUserId, 987L);
    }

    @Test
    public void getAllWorkoutsByUserId() {
        long currentUserId = 300L;
        long workoutId = 289L;
        LocalDate date = LocalDate.parse("2019-03-15");
        String description = "Running 5 miles";
        User currentUserMock = new User();
        currentUserMock.setId(currentUserId);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);
        Workout workoutMock = new Workout(workoutId, date, description, currentUserMock);
        when(workoutRepository.findAll()).thenReturn(Collections.singletonList(workoutMock));
        List<Workout> workouts = workoutService.getAllWorkoutsByUserId(currentUserId);

        assertThat(workouts.get(0).getId(), equalTo(workoutId));
        assertThat(workouts.get(0).getDate(), equalTo(date));
        assertThat(workouts.get(0).getDescription(), equalTo(description));
        assertThat(workouts.get(0).getUser(), equalTo(currentUserMock));
    }

    @Test
    public void getAllWorkoutsByAnotherUserId() {
        long workoutId = 289L;
        LocalDate date = LocalDate.parse("2019-03-15");
        String description = "Running 5 miles";
        User currentUserMock = new User();
        currentUserMock.setId(300L);
        when(userService.getCurrentUser()).thenReturn(currentUserMock);
        Workout workoutMock = new Workout(workoutId, date, description, currentUserMock);
        when(workoutRepository.findAll()).thenReturn(Collections.singletonList(workoutMock));

        exceptionRule.expect(AccessDeniedException.class);
        exceptionRule.expectMessage("Access denied");
        workoutService.getAllWorkoutsByUserId(100L);
    }
}