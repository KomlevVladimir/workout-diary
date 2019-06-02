package com.vladimirkomlev.workoutdiary.repository;

import com.vladimirkomlev.workoutdiary.model.ConfirmationSecret;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationSecretRepository extends JpaRepository<ConfirmationSecret, Long> {
    ConfirmationSecret findBySecret(String secret);
}
