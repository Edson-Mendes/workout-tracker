package br.com.emendes.workout_tracker_api.validation.validator;

import br.com.emendes.workout_tracker_api.model.WorkoutStatus;
import br.com.emendes.workout_tracker_api.validation.annotation.ValidWorkoutStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador de {@link ValidWorkoutStatus}.
 */
public class WorkoutStatusValidator implements ConstraintValidator<ValidWorkoutStatus, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) return true;
    try {
      WorkoutStatus.valueOf(value.toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

}
