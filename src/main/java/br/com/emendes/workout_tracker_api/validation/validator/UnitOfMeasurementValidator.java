package br.com.emendes.workout_tracker_api.validation.validator;

import br.com.emendes.workout_tracker_api.model.UnitOfMeasurement;
import br.com.emendes.workout_tracker_api.validation.annotation.ValidUnitOfMeasurement;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator de {@link ValidUnitOfMeasurement}.
 */
public class UnitOfMeasurementValidator implements ConstraintValidator<ValidUnitOfMeasurement, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) return true;
    try {
      UnitOfMeasurement.valueOf(value.toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

}
