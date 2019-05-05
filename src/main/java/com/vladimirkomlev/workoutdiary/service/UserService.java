package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByEmailIgnoreCase(String email);

    User findById(Long id);
}
