package br.com.emendes.workout_tracker_api.mapper.impl;

import br.com.emendes.workout_tracker_api.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker_api.mapper.WorkoutMapper;
import br.com.emendes.workout_tracker_api.model.WorkoutStatus;
import br.com.emendes.workout_tracker_api.model.entity.Workout;
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
        .status(WorkoutStatus.ONGOING)
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
        .status(workout.getStatus().toString())
        .createdAt(workout.getCreatedAt())
        .build();
  }

}
