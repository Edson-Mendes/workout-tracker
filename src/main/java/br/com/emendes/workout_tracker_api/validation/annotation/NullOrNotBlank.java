package br.com.emendes.workout_tracker_api.validation.annotation;

import br.com.emendes.workout_tracker_api.validation.validator.NullOrNotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * O elemento anotado não pode estar em branco.
 * <br><br>
 * Suporta tipo String.
 * <br><br>
 * Elementos {@code null} são considerados válidos.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {

  String message() default "must not be empty or blank";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
