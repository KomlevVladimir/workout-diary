package com.vladimirkomlev.workoutdiary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimirkomlev.workoutdiary.dto.AuthRequestDto;
import com.vladimirkomlev.workoutdiary.dto.AuthResponseDto;
import com.vladimirkomlev.workoutdiary.model.User;
import com.vladimirkomlev.workoutdiary.security.jwt.JwtTokenProvider;
import com.vladimirkomlev.workoutdiary.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {
    private MockMvc mockMvc;
    private AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
    private UserService userService = mock(UserService.class);
    private AuthController authController = new AuthController(authenticationManager, jwtTokenProvider, userService);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void getAuthToken() throws Exception {
        long userId = 123;
        String email = "test@myEmail.com";
        String mockToken = "token";
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail(email);
        when(userService.getUserByEmail(anyString())).thenReturn(mockUser);
        when(jwtTokenProvider.createToken(any(User.class))).thenReturn(mockToken);
        AuthRequestDto request = new AuthRequestDto();
        request.setEmail(email);
        AuthResponseDto response = new AuthResponseDto();
        response.setUserId(userId);
        response.setToken(mockToken);

        MvcResult mvcResult = mockMvc.perform(post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        
        String actualBody = mvcResult.getResponse().getContentAsString();
        String expectedBody = objectMapper.writeValueAsString(response);

        Assert.assertThat(actualBody, equalTo(expectedBody));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}