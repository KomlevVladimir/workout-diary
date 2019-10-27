package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.dto.WorkoutCreateUpdateRequestDto;
import com.vladimirkomlev.workoutdiary.exception.NotFoundException;
import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.model.Workout;
import com.vladimirkomlev.workoutdiary.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserService userService;

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository, UserService userService) {
        this.workoutRepository = workoutRepository;
        this.userService = userService;
    }

    @Override
    public List<Workout> getAllWorkoutsByUserId(Long userId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getId().equals(userId)) {
            return workoutRepository.findAll()
                    .stream()
                    .filter(workout -> workout.getUser().getId().equals(userId))
                    .collect(Collectors.toList());
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public Workout get(Long userId, Long workoutId) {
        User currentUser = userService.getCurrentUser();
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new NotFoundException("Workout not found"));
        if (currentUser.getId().equals(userId) & workout.getUser().getId().equals(userId)) {
            return workout;
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public Workout create(WorkoutCreateUpdateRequestDto workoutCreateUpdateRequestDto, Long userId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getId().equals(userId)) {
            Workout workout = new Workout();
            workout.setTitle(workoutCreateUpdateRequestDto.getTitle());
            workout.setDate(workoutCreateUpdateRequestDto.getDate());
            workout.setDescription(workoutCreateUpdateRequestDto.getDescription());
            workout.setUser(currentUser);
            return workoutRepository.save(workout);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public Workout update(WorkoutCreateUpdateRequestDto workoutCreateUpdateRequestDto, Long userId, Long workoutId) {
        User currentUser = userService.getCurrentUser();
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new NotFoundException("Workout not found"));
        if (currentUser.getId().equals(userId) & workout.getUser().getId().equals(userId)) {
            workout.setTitle(workoutCreateUpdateRequestDto.getTitle());
            workout.setDate(workoutCreateUpdateRequestDto.getDate());
            workout.setDescription(workoutCreateUpdateRequestDto.getDescription());
            return workoutRepository.save(workout);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public void delete(Long userId, Long workoutId) {
        User currentUser = userService.getCurrentUser();
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new NotFoundException("Workout not found"));
        if (currentUser.getId().equals(userId) & workout.getUser().getId().equals(userId)) {
            workoutRepository.delete(workout);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }
}
