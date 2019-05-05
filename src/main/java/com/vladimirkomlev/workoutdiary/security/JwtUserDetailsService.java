package com.vladimirkomlev.workoutdiary.security;

import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.security.jwt.JwtUserFactory;
import com.vladimirkomlev.workoutdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmailIgnoreCase(email);
        return JwtUserFactory.create(user);
    }
}
