package br.com.emendes.workout_tracker.unit.mapper.impl;

import br.com.emendes.workout_tracker.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker.mapper.impl.WorkoutMapperImpl;
import br.com.emendes.workout_tracker.model.entity.Workout;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit tests for WorkoutMapperImpl")
class WorkoutMapperImplTest {

  private final WorkoutMapperImpl workoutMapper = new WorkoutMapperImpl();

  @Test
  @DisplayName("toWorkout must return Workout when map successfully")
  void toWorkout_MustReturnWorkout_WhenMapSuccessfully() {
    WorkoutCreateRequest workoutCreateRequest = WorkoutCreateRequest.builder()
        .name("Leg day")
        .description("Lower body focused workout")
        .build();
    Workout actualWorkout = workoutMapper.toWorkout(workoutCreateRequest);

    assertThat(actualWorkout).isNotNull()
        .hasFieldOrPropertyWithValue("name", "Leg day")
        .hasFieldOrPropertyWithValue("description", "Lower body focused workout")
        .hasFieldOrPropertyWithValue("isInUse", true);
    assertThat(actualWorkout.getId()).isNull();
    assertThat(actualWorkout.getCreatedAt()).isNotNull();
  }

  @Test
  @DisplayName("toWorkout must throw IllegalArgumentException when workoutCreateRequest is null")
  void toWorkout_MustThrowIllegalArgumentException_WhenWorkoutCreateRequestIsNull() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> workoutMapper.toWorkout(null))
        .withMessage("workoutCreateRequest must not be null");
  }

  @Test
  @DisplayName("toWorkoutResponse must return Workout when map successfully")
  void toWorkoutResponse_MustReturnWorkout_WhenMapSuccessfully() {
    Workout workout = Workout.builder()
        .id(1_000L)
        .name("Leg day")
        .description("Lower body focused workout")
        .isInUse(true)
        .createdAt(LocalDateTime.parse("2025-05-12T10:00:00"))
        .build();
    WorkoutResponse actualWorkoutResponse = workoutMapper.toWorkoutResponse(workout);

    assertThat(actualWorkoutResponse).isNotNull()
        .hasFieldOrPropertyWithValue("id", 1_000L)
        .hasFieldOrPropertyWithValue("name", "Leg day")
        .hasFieldOrPropertyWithValue("description", "Lower body focused workout")
        .hasFieldOrPropertyWithValue("isInUse", true)
        .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T10:00:00"));
  }

  @Test
  @DisplayName("toWorkoutResponse must throw IllegalArgumentException when workoutCreateRequest is null")
  void toWorkoutResponse_MustThrowIllegalArgumentException_WhenWorkoutCreateRequestIsNull() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> workoutMapper.toWorkoutResponse(null))
        .withMessage("workout must not be null");
  }

}