package br.com.emendes.workout_tracker_api.validation.annotation;

import br.com.emendes.workout_tracker_api.validation.validator.WorkoutStatusValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Verifica se o elemento anotado é um WorkoutStatus.<br>
 * <br>
 * WorkoutStatus válidos: ONGOING, FINISHED.<br>
 * Letras minúsculas também são aceitas (i.e. Ongoing, OnGoInG são valores válidos)
 * <br><br>
 * Suporta tipo String.
 * <br><br>
 * Elementos {@code null} são considerados válidos.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WorkoutStatusValidator.class)
public @interface ValidWorkoutStatus {

  String message() default "must be a valid WorkoutStatus";

  Class<?>[] groups() default {};

  Class<?>[] payload() default {};

}
