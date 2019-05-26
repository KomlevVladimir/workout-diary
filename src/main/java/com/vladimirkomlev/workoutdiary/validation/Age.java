package com.vladimirkomlev.workoutdiary.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull(message = "should not be null")
@Digits(integer=3, fraction=0, message = "no more than 3 digits")
@Constraint(validatedBy = {})
@Target(FIELD)
@Retention(RUNTIME)
public @interface Age {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
