package br.com.emendes.workout_tracker_api.service.impl;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseDetailsResponse;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.exception.ExerciseNotFoundException;
import br.com.emendes.workout_tracker_api.mapper.ExerciseMapper;
import br.com.emendes.workout_tracker_api.model.entity.Exercise;
import br.com.emendes.workout_tracker_api.model.entity.Weight;
import br.com.emendes.workout_tracker_api.repository.ExerciseRepository;
import br.com.emendes.workout_tracker_api.service.ExerciseService;
import br.com.emendes.workout_tracker_api.service.WeightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementação de {@link ExerciseService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseServiceImpl implements ExerciseService {

  private final ExerciseMapper exerciseMapper;
  private final ExerciseRepository exerciseRepository;
  private final WeightService weightService;

  @Override
  public ExerciseResponse create(Long workoutId, ExerciseCreateRequest exerciseCreateRequest) {
    log.info("attempt to create Exercise");
    Exercise exercise = exerciseMapper.toExercise(workoutId, exerciseCreateRequest);
    exercise = exerciseRepository.save(exercise);
    log.info("exercise created successfully with id: {}", exercise.getId());

    return exerciseMapper.toExerciseResponse(exercise);
  }

  @Override
  public WeightResponse addWeight(Long exerciseId, WeightCreateRequest weightCreateRequest) {
    log.info("attempt to add Weight to exercise with id: {}", exerciseId);
    Optional<Exercise> exerciseOptional = exerciseRepository.findById(exerciseId);
    if (exerciseOptional.isPresent()) {
      WeightResponse weightResponse = weightService.create(exerciseId, weightCreateRequest);
      Exercise exercise = exerciseOptional.get();
      exercise.setWeight(Weight.builder().id(weightResponse.id()).build());
      exerciseRepository.save(exercise);
      log.info("weight with id: {} added successfully into exercise with id: {}", weightResponse.id(), exerciseId);

      return weightResponse;
    }

    throw new ExerciseNotFoundException("exercise not found with id: %s".formatted(exerciseId));
  }

  @Override
  public Page<ExerciseDetailsResponse> fetchExercises(Long workoutId, Pageable pageable) {
    log.info("attempt to fetch Exercises with workoutId: {}", workoutId);

    return exerciseRepository.findByWorkoutId(workoutId, pageable)
        .map(exerciseMapper::toExerciseDetailsResponse);
  }

}
