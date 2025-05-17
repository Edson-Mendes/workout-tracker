package br.com.emendes.workout_tracker_api.unit.dto.request;

import br.com.emendes.workout_tracker_api.dto.request.WorkoutCreateRequest;
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
import static org.assertj.core.api.Assumptions.assumeThat;

@DisplayName("Unit tests das validações do DTO WorkoutCreateRequest")
class WorkoutCreateRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Nested
  @DisplayName("Tests for name validation")
  class NameValidation {

    public static final String NAME_PROPERTY = "name";

    @ParameterizedTest
    @ValueSource(strings = {"A", "namewith100characters____namewith100characters____namewith100characters____namewith100characters____"})
    @DisplayName("name validation must not return violations when name is valid")
    void nameValidation_MustNotReturnViolations_WhenNameIsValid(String validName) {
      assumeThat(validName).isNotBlank().hasSizeBetween(1, 100);

      WorkoutCreateRequest workoutCreateRequest = WorkoutCreateRequest.builder()
          .name(validName)
          .build();

      Set<ConstraintViolation<WorkoutCreateRequest>> actualViolations = validator
          .validateProperty(workoutCreateRequest, NAME_PROPERTY);

      assertThat(actualViolations).isNotNull().isEmpty();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"   ", "\t", "\n", ""})
    @DisplayName("name validation must return violations when name is null or blank")
    void nameValidation_MustReturnViolations_WhenNameIsNullOrBlank(String invalidName) {
      WorkoutCreateRequest workoutCreateRequest = WorkoutCreateRequest.builder()
          .name(invalidName)
          .build();

      Set<ConstraintViolation<WorkoutCreateRequest>> actualViolations = validator
          .validateProperty(workoutCreateRequest, NAME_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("name must not be blank or null");
    }

    @Test
    @DisplayName("name validation must return violations when name size is greater than 100")
    void nameValidation_MustReturnViolations_WhenNameSizeIsGreaterThan100() {
      String nameWithMoreThan100Characters = "nameWithMoreThan100Characters____nameWithMoreThan100Characters____nameWithMoreThan100Characters______";
      assumeThat(nameWithMoreThan100Characters).isNotBlank().hasSizeGreaterThan(100);

      WorkoutCreateRequest workoutCreateRequest = WorkoutCreateRequest.builder()
          .name(nameWithMoreThan100Characters)
          .build();

      Set<ConstraintViolation<WorkoutCreateRequest>> actualViolations = validator
          .validateProperty(workoutCreateRequest, NAME_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("name must contain between 1 and 100 characters long");
    }

  }

  @Nested
  @DisplayName("Tests for description validation")
  class DescriptionValidation {

    private static final String DESCRIPTION_PROPERTY = "description";
    public static final String DESCRIPTION_WITH_255_CHARACTERS_LONG =
        "description with 255 characters long_____" +
        "description with 255 characters long_____" +
        "description with 255 characters long_____" +
        "description with 255 characters long_____" +
        "description with 255 characters long_____" +
        "description with 255 characters long________";

    @ParameterizedTest
    @ValueSource(strings = {"lorem ipsum dolor sit amet", DESCRIPTION_WITH_255_CHARACTERS_LONG})
    @DisplayName("description validation must not return Violations when description is valid")
    void descriptionValidation_MustNotReturnViolations_WhenDescriptionIsValid(String validDescription) {
      assumeThat(validDescription).isNotNull().hasSizeLessThanOrEqualTo(255);

      WorkoutCreateRequest workoutCreateRequest = WorkoutCreateRequest.builder()
          .description(validDescription)
          .build();

      Set<ConstraintViolation<WorkoutCreateRequest>> actualViolations = validator
          .validateProperty(workoutCreateRequest, DESCRIPTION_PROPERTY);

      assertThat(actualViolations).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("description validation must not return Violations when description is NULL")
    void descriptionValidation_MustNotReturnViolations_WhenDescriptionIsNull() {
      WorkoutCreateRequest workoutCreateRequest = WorkoutCreateRequest.builder()
          .description(null)
          .build();

      Set<ConstraintViolation<WorkoutCreateRequest>> actualViolations = validator
          .validateProperty(workoutCreateRequest, DESCRIPTION_PROPERTY);

      assertThat(actualViolations).isNotNull().isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"   ", "\t", "\n", ""})
    @DisplayName("description validation must return Violations when description is blank")
    void descriptionValidation_MustReturnViolations_WhenDescriptionIsBlank(String invalidDescription) {
      WorkoutCreateRequest workoutCreateRequest = WorkoutCreateRequest.builder()
          .description(invalidDescription)
          .build();

      Set<ConstraintViolation<WorkoutCreateRequest>> actualViolations = validator
          .validateProperty(workoutCreateRequest, DESCRIPTION_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("description must be null or not be blank");
    }

    @Test
    @DisplayName("description validation must return Violations when description size is greater than 255")
    void descriptionValidation_MustReturnViolations_WhenDescriptionSizeIsGreaterThan255() {
      String descriptionWith256CharactersLong =
          "descriptionwith256characterslongdescriptionwith256characterslong" +
          "descriptionwith256characterslongdescriptionwith256characterslong" +
          "descriptionwith256characterslongdescriptionwith256characterslong" +
          "descriptionwith256characterslongdescriptionwith256characterslong";
      assumeThat(descriptionWith256CharactersLong).isNotNull().hasSize(256);
      WorkoutCreateRequest workoutCreateRequest = WorkoutCreateRequest.builder()
          .description(descriptionWith256CharactersLong)
          .build();

      Set<ConstraintViolation<WorkoutCreateRequest>> actualViolations = validator
          .validateProperty(workoutCreateRequest, DESCRIPTION_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("description must contain max 255 characters long");
    }

  }

}