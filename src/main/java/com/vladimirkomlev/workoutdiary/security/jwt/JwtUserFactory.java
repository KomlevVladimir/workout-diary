package com.vladimirkomlev.workoutdiary.security.jwt;

import com.vladimirkomlev.workoutdiary.model.User;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                user.getEmail(),
                user.getPassword()
        );
    }
}