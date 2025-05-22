package br.com.emendes.workout_tracker_api.mapper.impl;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.mapper.ExerciseMapper;
import br.com.emendes.workout_tracker_api.model.entity.Exercise;
import br.com.emendes.workout_tracker_api.model.entity.Workout;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * Implementação de {@link ExerciseMapper}.
 */
@Component
public class ExerciseMapperImpl implements ExerciseMapper {

  @Override
  public Exercise toExercise(Long workoutId, ExerciseCreateRequest exerciseCreateRequest) {
    Assert.notNull(exerciseCreateRequest, "exerciseCreateRequest must not be null");
    Assert.notNull(workoutId, "workoutId must not be null");

    return Exercise.builder()
        .name(exerciseCreateRequest.name())
        .description(exerciseCreateRequest.description())
        .additional(exerciseCreateRequest.additional())
        .sets(exerciseCreateRequest.sets())
        .createdAt(LocalDateTime.now())
        .workout(Workout.builder().id(workoutId).build())
        .build();
  }

  @Override
  public ExerciseResponse toExerciseResponse(Exercise exercise) {
    Assert.notNull(exercise, "exercise must not be null");

    return ExerciseResponse.builder()
        .id(exercise.getId())
        .name(exercise.getName())
        .description(exercise.getDescription())
        .additional(exercise.getAdditional())
        .sets(exercise.getSets())
        .createdAt(exercise.getCreatedAt())
        .updatedAt(exercise.getUpdatedAt())
        .build();
  }

}
