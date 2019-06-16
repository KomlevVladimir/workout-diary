package com.vladimirkomlev.workoutdiary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(1)
public class SwaggerSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String AUTH_ROLE = "SWAGGER";

    private final PasswordEncoder passwordEncoder;
    @Value("${swagger.ui.user}")
    private String user;

    @Value("${swagger.ui.password}")
    private String password;

    public SwaggerSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs", "/login")
                .and()
                .authorizeRequests()
                .anyRequest().hasRole(AUTH_ROLE)
                .and()
                .formLogin();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser(user)
                .password(passwordEncoder.encode(password))
                .roles(AUTH_ROLE);
    }
}
