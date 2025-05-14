package br.com.emendes.workout_tracker.mapper.impl;

import br.com.emendes.workout_tracker.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker.mapper.WorkoutMapper;
import br.com.emendes.workout_tracker.model.entity.Workout;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * Implementação de {@link WorkoutMapper}.
 */
@Component
public class WorkoutMapperImpl implements WorkoutMapper {

  @Override
  public Workout toWorkout(WorkoutCreateRequest workoutCreateRequest) {
    Assert.notNull(workoutCreateRequest, "workoutCreateRequest must not be null");

    return Workout.builder()
        .name(workoutCreateRequest.name())
        .description(workoutCreateRequest.description())
        .isInUse(true)
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Override
  public WorkoutResponse toWorkoutResponse(Workout workout) {
    Assert.notNull(workout, "workout must not be null");

    return WorkoutResponse.builder()
        .id(workout.getId())
        .name(workout.getName())
        .description(workout.getDescription())
        .isInUse(workout.isInUse())
        .createdAt(workout.getCreatedAt())
        .build();
  }

}
