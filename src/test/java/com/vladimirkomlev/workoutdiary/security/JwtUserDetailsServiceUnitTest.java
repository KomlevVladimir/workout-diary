package com.vladimirkomlev.workoutdiary.security;

import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.security.jwt.JwtUser;
import com.vladimirkomlev.workoutdiary.service.UserService;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtUserDetailsServiceUnitTest {
    private UserService userService = mock(UserService.class);
    private JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService(userService);

    @Test
    public void loadUserByUsername() {
        String email = "test@myemail.com";
        String password = "Password!1";
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(password);
        when(userService.getUserByEmail(email)).thenReturn(mockUser);
        JwtUser jwtUser = (JwtUser) jwtUserDetailsService.loadUserByUsername(email);

        assertThat(jwtUser.getUsername(), equalTo(email));
        assertThat(jwtUser.getPassword(), equalTo(password));
    }
}