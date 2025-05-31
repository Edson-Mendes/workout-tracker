package br.com.emendes.workout_tracker_api.unit.mapper.impl;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseDetailsResponse;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.mapper.impl.ExerciseMapperImpl;
import br.com.emendes.workout_tracker_api.mapper.impl.WeightMapperImpl;
import br.com.emendes.workout_tracker_api.model.UnitOfMeasurement;
import br.com.emendes.workout_tracker_api.model.entity.Exercise;
import br.com.emendes.workout_tracker_api.model.entity.Weight;
import br.com.emendes.workout_tracker_api.model.entity.Workout;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit tests for ExerciseMapperImpl")
class ExerciseMapperImplTest {

  private final ExerciseMapperImpl exerciseMapper = new ExerciseMapperImpl(new WeightMapperImpl());

  @Test
  @DisplayName("toExercise must return Exercise when map successfully")
  void toExercise_MustReturnExercise_WhenMapSuccessfully() {
    ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
        .name("Leg press")
        .description("Exercise that focuses on the quadriceps")
        .additional("Apply the Rest in Pause technique in the last set")
        .sets(4)
        .build();
    Exercise actualExercise = exerciseMapper.toExercise(1_000L, exerciseCreateRequest);

    assertThat(actualExercise).isNotNull()
        .hasFieldOrPropertyWithValue("name", "Leg press")
        .hasFieldOrPropertyWithValue("description", "Exercise that focuses on the quadriceps")
        .hasFieldOrPropertyWithValue("additional", "Apply the Rest in Pause technique in the last set")
        .hasFieldOrPropertyWithValue("sets", 4);
    assertThat(actualExercise.getId()).isNull();
    assertThat(actualExercise.getCreatedAt()).isNotNull();
    assertThat(actualExercise.getWorkout()).isNotNull()
        .hasFieldOrPropertyWithValue("id", 1_000L);
  }

  @Test
  @DisplayName("toExercise must throw IllegalArgumentException when exerciseCreateRequest is null")
  void toExercise_MustThrowIllegalArgumentException_WhenExerciseCreateRequestIsNull() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> exerciseMapper.toExercise(1_000L, null))
        .withMessage("exerciseCreateRequest must not be null");
  }

  @Test
  @DisplayName("toExercise must throw IllegalArgumentException when workoutId is null")
  void toExercise_MustThrowIllegalArgumentException_WhenWorkoutIdIsNull() {
    ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
        .name("Leg press")
        .description("Exercise that focuses on the quadriceps")
        .additional("Apply the Rest in Pause technique in the last set")
        .sets(4)
        .build();

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> exerciseMapper.toExercise(null, exerciseCreateRequest))
        .withMessage("workoutId must not be null");
  }

  @Test
  @DisplayName("toExerciseResponse must return Exercise when map successfully")
  void toExerciseResponse_MustReturnExercise_WhenMapSuccessfully() {
    Exercise exercise = Exercise.builder()
        .id(1_000_000L)
        .name("Leg press")
        .description("Exercise that focuses on the quadriceps")
        .additional("Apply the Rest in Pause technique in the last set")
        .sets(4)
        .createdAt(LocalDateTime.parse("2025-05-12T10:00:00"))
        .workout(Workout.builder().id(1_000L).build())
        .build();
    ExerciseResponse actualExerciseResponse = exerciseMapper.toExerciseResponse(exercise);

    assertThat(actualExerciseResponse).isNotNull()
        .hasFieldOrPropertyWithValue("id", 1_000_000L)
        .hasFieldOrPropertyWithValue("name", "Leg press")
        .hasFieldOrPropertyWithValue("description", "Exercise that focuses on the quadriceps")
        .hasFieldOrPropertyWithValue("additional", "Apply the Rest in Pause technique in the last set")
        .hasFieldOrPropertyWithValue("sets", 4)
        .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T10:00:00"))
        .hasFieldOrPropertyWithValue("updatedAt", null);
  }

  @Test
  @DisplayName("toExerciseResponse must throw IllegalArgumentException when exercise is null")
  void toExerciseResponse_MustThrowIllegalArgumentException_WhenExerciseIsNull() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> exerciseMapper.toExerciseResponse(null))
        .withMessage("exercise must not be null");
  }

  @Test
  @DisplayName("toExerciseDetailsResponse must return ExerciseDetailsResponse with weight when exercise has weight")
  void toExerciseDetailsResponse_MustReturnExerciseDetailsResponseWithWeight_WhenExerciseHasWeight() {
    Weight weight = Weight.builder()
        .id(1_000_000_000L)
        .value(new BigDecimal("60.0"))
        .unit(UnitOfMeasurement.KILOGRAMS)
        .createdAt(LocalDateTime.parse("2025-05-12T12:00:00"))
        .exercise(Exercise.builder().id(1_000_000L).build())
        .build();
    Exercise exercise = Exercise.builder()
        .id(1_000_000L)
        .name("Leg press")
        .description("Exercise that focuses on the quadriceps")
        .additional("Apply the Rest in Pause technique in the last set")
        .sets(4)
        .createdAt(LocalDateTime.parse("2025-05-12T10:00:00"))
        .weight(weight)
        .workout(Workout.builder().id(1_000L).build())
        .build();

    ExerciseDetailsResponse actualExerciseDetailsResponse = exerciseMapper.toExerciseDetailsResponse(exercise);

    assertThat(actualExerciseDetailsResponse).isNotNull();
    assertThat(actualExerciseDetailsResponse.exercise()).isNotNull()
        .hasFieldOrPropertyWithValue("id", 1_000_000L)
        .hasFieldOrPropertyWithValue("name", "Leg press")
        .hasFieldOrPropertyWithValue("description", "Exercise that focuses on the quadriceps")
        .hasFieldOrPropertyWithValue("additional", "Apply the Rest in Pause technique in the last set")
        .hasFieldOrPropertyWithValue("sets", 4)
        .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T10:00:00"))
        .hasFieldOrPropertyWithValue("updatedAt", null);
    assertThat(actualExerciseDetailsResponse.weight()).isNotNull();
  }

  @Test
  @DisplayName("toExerciseDetailsResponse must return ExerciseDetailsResponse without weight when exercise.weight is null")
  void toExerciseDetailsResponse_MustReturnExerciseDetailsResponseWithoutWeight_WhenExerciseWeightIsNull() {
    Exercise exercise = Exercise.builder()
        .id(1_000_000L)
        .name("Leg press")
        .description("Exercise that focuses on the quadriceps")
        .additional("Apply the Rest in Pause technique in the last set")
        .sets(4)
        .createdAt(LocalDateTime.parse("2025-05-12T10:00:00"))
        .workout(Workout.builder().id(1_000L).build())
        .build();

    ExerciseDetailsResponse actualExerciseDetailsResponse = exerciseMapper.toExerciseDetailsResponse(exercise);

    assertThat(actualExerciseDetailsResponse).isNotNull();
    assertThat(actualExerciseDetailsResponse.exercise()).isNotNull()
        .hasFieldOrPropertyWithValue("id", 1_000_000L)
        .hasFieldOrPropertyWithValue("name", "Leg press")
        .hasFieldOrPropertyWithValue("description", "Exercise that focuses on the quadriceps")
        .hasFieldOrPropertyWithValue("additional", "Apply the Rest in Pause technique in the last set")
        .hasFieldOrPropertyWithValue("sets", 4)
        .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T10:00:00"))
        .hasFieldOrPropertyWithValue("updatedAt", null);
    assertThat(actualExerciseDetailsResponse.weight()).isNull();
  }

  @Test
  @DisplayName("toExerciseDetailsResponse must throw IllegalArgumentException when exercise is null")
  void toExerciseDetailsResponse_MustThrowIllegalArgumentException_WhenExerciseIsNull() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> exerciseMapper.toExerciseDetailsResponse(null))
        .withMessage("exercise must not be null");
  }

}