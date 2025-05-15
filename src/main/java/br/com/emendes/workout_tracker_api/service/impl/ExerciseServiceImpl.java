package br.com.emendes.workout_tracker_api.service.impl;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.mapper.ExerciseMapper;
import br.com.emendes.workout_tracker_api.model.entity.Exercise;
import br.com.emendes.workout_tracker_api.repository.ExerciseRepository;
import br.com.emendes.workout_tracker_api.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementação de {@link ExerciseService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseServiceImpl implements ExerciseService {

  private final ExerciseMapper exerciseMapper;
  private final ExerciseRepository exerciseRepository;

  @Override
  public ExerciseResponse create(Long workoutId, ExerciseCreateRequest exerciseCreateRequest) {
    log.info("attempt to create Exercise");
    Exercise exercise = exerciseMapper.toExercise(workoutId, exerciseCreateRequest);
    exercise = exerciseRepository.save(exercise);
    log.info("exercise created successfully with id: {}", exercise.getId());

    return exerciseMapper.toExerciseResponse(exercise);
  }

}
