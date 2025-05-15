package br.com.emendes.workout_tracker_api.validation.validator;

import br.com.emendes.workout_tracker_api.validation.annotation.NullOrNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator de {@link NullOrNotBlank}.
 */
public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) return true;
    return !value.isBlank();
  }

}
