package br.com.emendes.workout_tracker_api.service.impl;

import br.com.emendes.workout_tracker_api.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker_api.mapper.WorkoutMapper;
import br.com.emendes.workout_tracker_api.model.entity.Workout;
import br.com.emendes.workout_tracker_api.repository.WorkoutRepository;
import br.com.emendes.workout_tracker_api.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementação de {@link WorkoutService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutServiceImpl implements WorkoutService {

  private final WorkoutRepository workoutRepository;
  private final WorkoutMapper workoutMapper;

  @Override
  public WorkoutResponse create(WorkoutCreateRequest workoutCreateRequest) {
    log.info("attempt to create workout");
    Workout workout = workoutMapper.toWorkout(workoutCreateRequest);
    workout = workoutRepository.save(workout);
    log.info("workout created successfully with id: {}", workout.getId());

    return workoutMapper.toWorkoutResponse(workout);
  }

}
