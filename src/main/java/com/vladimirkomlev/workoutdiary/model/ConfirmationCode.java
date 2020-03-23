package com.vladimirkomlev.workoutdiary.model;

import javax.persistence.*;
import java.util.Random;

@Entity
@Table(name = "confirmation_codes")
public class ConfirmationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "code")
    private String code;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationCode() {
    }

    public ConfirmationCode(User user) {
        this.user = user;
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        code = String.format("%06d", number);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
