package br.com.emendes.workout_tracker_api.validation.annotation;

import br.com.emendes.workout_tracker_api.validation.validator.UnitOfMeasurementValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Verifica se o elemento anotado é uma unidade de medida.<br>
 * <br>
 * Unidades válidas: KILOGRAMS, POUNDS, HOURS, MINUTES.<br>
 * Letras minúsculas também são aceitas (i.e. Kilograms, KiLoGrAms são valores válidos)
 * <br><br>
 * Suporta tipo String.
 * <br><br>
 * Elementos {@code null} são considerados válidos.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UnitOfMeasurementValidator.class)
public @interface ValidUnitOfMeasurement {

  String message() default "must be a valid unit of measurement";

  Class<?>[] groups() default {};

  Class<?>[] payload() default {};

}
