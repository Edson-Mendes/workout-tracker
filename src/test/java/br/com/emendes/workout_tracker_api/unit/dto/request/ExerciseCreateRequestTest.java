package br.com.emendes.workout_tracker_api.unit.dto.request;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
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

@DisplayName("Unit tests das validações do DTO ExerciseCreateRequest")
class ExerciseCreateRequestTest {

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

      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .name(validName)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, NAME_PROPERTY);

      assertThat(actualViolations).isNotNull().isEmpty();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"   ", "\t", "\n", ""})
    @DisplayName("name validation must return violations when name is null or blank")
    void nameValidation_MustReturnViolations_WhenNameIsNullOrBlank(String invalidName) {
      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .name(invalidName)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, NAME_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("name must not be blank or null");
    }

    @Test
    @DisplayName("name validation must return violations when name size is greater than 100")
    void nameValidation_MustReturnViolations_WhenNameSizeIsGreaterThan100() {
      String nameWithMoreThan100Characters = "nameWithMoreThan100Characters____nameWithMoreThan100Characters____nameWithMoreThan100Characters______";
      assumeThat(nameWithMoreThan100Characters).isNotBlank().hasSizeGreaterThan(100);

      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .name(nameWithMoreThan100Characters)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, NAME_PROPERTY);

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

      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .description(validDescription)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, DESCRIPTION_PROPERTY);

      assertThat(actualViolations).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("description validation must not return Violations when description is NULL")
    void descriptionValidation_MustNotReturnViolations_WhenDescriptionIsNull() {
      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .description(null)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, DESCRIPTION_PROPERTY);

      assertThat(actualViolations).isNotNull().isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"   ", "\t", "\n", ""})
    @DisplayName("description validation must return Violations when description is blank")
    void descriptionValidation_MustReturnViolations_WhenDescriptionIsBlank(String invalidDescription) {
      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .description(invalidDescription)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, DESCRIPTION_PROPERTY);

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
      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .description(descriptionWith256CharactersLong)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, DESCRIPTION_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("description must contain max 255 characters long");
    }

  }

  @Nested
  @DisplayName("Tests for additional validation")
  class AdditionalValidation {

    private static final String ADDITIONAL_PROPERTY = "additional";
    private static final String ADDITIONAL_WITH_150_CHARACTERS_LONG =
        "additionalWith150CharactersLong______" +
        "additionalWith150CharactersLong______" +
        "additionalWith150CharactersLong______" +
        "additionalWith150CharactersLong________";

    @ParameterizedTest
    @ValueSource(strings = {"A", ADDITIONAL_WITH_150_CHARACTERS_LONG})
    @DisplayName("additional validation must not return violations when name is valid")
    void additionalValidation_MustNotReturnViolations_WhenNameIsValid(String validAdditional) {
      assumeThat(validAdditional).isNotBlank().hasSizeBetween(1, 150);

      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .additional(validAdditional)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, ADDITIONAL_PROPERTY);

      assertThat(actualViolations).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("additional validation must not return Violations when additional is NULL")
    void additionalValidation_MustNotReturnViolations_WhenAdditionalIsNull() {
      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .additional(null)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, ADDITIONAL_PROPERTY);

      assertThat(actualViolations).isNotNull().isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"   ", "\t", "\n", ""})
    @DisplayName("additional validation must return Violations when additional is blank")
    void additionalValidation_MustReturnViolations_WhenAdditionalIsBlank(String invalidAdditional) {
      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .additional(invalidAdditional)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, ADDITIONAL_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("additional must be null or not be blank");
    }

    @Test
    @DisplayName("additional validation must return Violations when additional size is greater than 255")
    void additionalValidation_MustReturnViolations_WhenAdditionalSizeIsGreaterThan255() {
      String additionalWith151CharactersLong =
          "additionalWith151CharactersLong______" +
          "additionalWith151CharactersLong______" +
          "additionalWith151CharactersLong______" +
          "additionalWith151CharactersLong_________";
      assumeThat(additionalWith151CharactersLong).isNotNull().hasSize(151);
      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .additional(additionalWith151CharactersLong)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, ADDITIONAL_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("additional must contain between 1 and 150 characters long");
    }

  }

  @Nested
  @DisplayName("Tests for sets validation")
  class SetsValidation {

    private static final String SETS_PROPERTY = "sets";

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1_000_000, Integer.MAX_VALUE})
    @DisplayName("sets validation must not return Violations when sets is valid")
    void setsValidation_MustNotReturnViolations_WhenSetsIsValid(int validSets) {
      assumeThat(validSets).isBetween(1, Integer.MAX_VALUE);

      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .sets(validSets)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, SETS_PROPERTY);

      assertThat(actualViolations).isNotNull().isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -1_000_000, Integer.MIN_VALUE})
    @DisplayName("sets validation must return Violations when sets is non positive")
    void setsValidation_MustNotReturnViolations_WhenSetsIsNonPositive(int invalidSets) {
      assumeThat(invalidSets).isLessThan(1);

      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .sets(invalidSets)
          .build();

      Set<ConstraintViolation<ExerciseCreateRequest>> actualViolations = validator
          .validateProperty(exerciseCreateRequest, SETS_PROPERTY);

      assertThat(actualViolations).isNotNull().isNotEmpty();
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(actualMessages).isNotNull().contains("sets must be positive");
    }

  }

}