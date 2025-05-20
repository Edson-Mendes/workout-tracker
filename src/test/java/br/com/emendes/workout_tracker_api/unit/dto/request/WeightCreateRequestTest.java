package br.com.emendes.workout_tracker_api.unit.dto.request;

import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit tests das validações do DTO WeightCreateRequest")
class WeightCreateRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Nested
  @DisplayName("Tests for value validation")
  class ValueValidation {

    private final String VALUE_PROPERTY = "value";

    @ParameterizedTest
    @ValueSource(strings = {"1", "10", "100", "1000", "1.5", "10.5", "100.5", "1000.5"})
    @DisplayName("value validation must not return Violations when value is valid")
    void valueValidation_MustNotReturnViolations_WhenValueIsValid(String validValue) {
      WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
          .value(validValue)
          .build();

      Set<ConstraintViolation<WeightCreateRequest>> actualViolations = validator
          .validateProperty(weightCreateRequest, VALUE_PROPERTY);

      assertThat(actualViolations).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("value validation must return Violations when integer part has more than 4 digits")
    void valueValidation_MustReturnViolations_WhenIntegerPartHasMoreThan4Digits() {
      WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
          .value("10000")
          .build();

      Set<ConstraintViolation<WeightCreateRequest>> actualViolations = validator
          .validateProperty(weightCreateRequest, VALUE_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("value is out of bounds (<4 digits>.<1 digits>)");
    }

    @Test
    @DisplayName("value validation must return Violations when fraction part has more than 1 digits")
    void valueValidation_MustReturnViolations_WhenFractionPartHasMoreThan1Digits() {
      WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
          .value("0.15")
          .build();

      Set<ConstraintViolation<WeightCreateRequest>> actualViolations = validator
          .validateProperty(weightCreateRequest, VALUE_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("value is out of bounds (<4 digits>.<1 digits>)");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"   ", "\t", "\n", ""})
    @DisplayName("value validation must return Violations when value is Null or blank")
    void valueValidation_MustReturnViolations_WhenValueIsNullOrBlank(String blankValue) {
      WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
          .value(blankValue)
          .build();

      Set<ConstraintViolation<WeightCreateRequest>> actualViolations = validator
          .validateProperty(weightCreateRequest, VALUE_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("value must not be blank");
    }

  }

  @Nested
  @DisplayName("Tests for unit validation")
  class UnitValidation {

    private final String UNIT_PROPERTY = "unit";

    @ParameterizedTest
    @ValueSource(strings = {"KILOGRAMS", "Kilograms", "KiLoGrAmS", "POUNDS", "HOURS", "minutes"})
    @DisplayName("unit validation must not return Violations when unit is valid")
    void unitValidation_MustNotReturnViolations_WhenUnitIsValid(String validUnit) {
      WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
          .unit(validUnit)
          .build();

      Set<ConstraintViolation<WeightCreateRequest>> actualViolations = validator
          .validateProperty(weightCreateRequest, UNIT_PROPERTY);

      assertThat(actualViolations).isNotNull().isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"   ", "\t", "\n", ""})
    @NullSource
    @DisplayName("unit validation must return Violations when unit is blank")
    void unitValidation_MustReturnViolations_WhenUnitIsBlank(String blankUnit) {
      WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
          .unit(blankUnit)
          .build();

      Set<ConstraintViolation<WeightCreateRequest>> actualViolations = validator
          .validateProperty(weightCreateRequest, UNIT_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("unit must not be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"KILOGRAm", "lorem", "K I L O G R A M S", "K ilograms"})
    @DisplayName("unit validation must return Violations when unit is not a valid unit of measurement")
    void unitValidation_MustReturnViolations_WhenUnitIsNotAValidUnitOfMeasurement(String invalidUnit) {
      WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
          .unit(invalidUnit)
          .build();

      Set<ConstraintViolation<WeightCreateRequest>> actualViolations = validator
          .validateProperty(weightCreateRequest, UNIT_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("unit must be a valid unit of measurement (i.e. KILOGRAM, POUNDS, HOURS, MINUTES");
    }

  }

}