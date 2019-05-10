package com.vladimirkomlev.workoutdiary.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "confirmation_secrets")
public class ConfirmationSecret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "secret")
    private String secret;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationSecret() {
    }

    public ConfirmationSecret(User user) {
        this.user = user;
        secret = UUID.randomUUID().toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
