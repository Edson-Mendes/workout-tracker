package br.com.emendes.workout_tracker_api.service.impl;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseDetailsResponse;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker_api.exception.WorkoutNotFoundException;
import br.com.emendes.workout_tracker_api.mapper.WorkoutMapper;
import br.com.emendes.workout_tracker_api.model.WorkoutStatus;
import br.com.emendes.workout_tracker_api.model.entity.Workout;
import br.com.emendes.workout_tracker_api.repository.WorkoutRepository;
import br.com.emendes.workout_tracker_api.service.ExerciseService;
import br.com.emendes.workout_tracker_api.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  private final ExerciseService exerciseService;

  @Override
  public WorkoutResponse create(WorkoutCreateRequest workoutCreateRequest) {
    log.info("attempt to create workout");
    Workout workout = workoutMapper.toWorkout(workoutCreateRequest);
    workout = workoutRepository.save(workout);
    log.info("workout created successfully with id: {}", workout.getId());

    return workoutMapper.toWorkoutResponse(workout);
  }

  @Override
  public ExerciseResponse addExercise(Long workoutId, ExerciseCreateRequest exerciseCreateRequest) {
    log.info("attempt to add exercise to workout with id: {}", workoutId);
    verifyIfExistsWorkout(workoutId);

    ExerciseResponse exerciseResponse = exerciseService.create(workoutId, exerciseCreateRequest);
    log.info("exercise added successfully with id: {}", exerciseResponse.id());
    return exerciseResponse;
  }

  @Override
  public WeightResponse addWeight(Long workoutId, Long exerciseId, WeightCreateRequest weightCreateRequest) {
    log.info("attempt to add weight to the exercise belonging to workout with id: {}", workoutId);
    verifyIfExistsWorkout(workoutId);

    return exerciseService.addWeight(exerciseId, weightCreateRequest);
  }

  @Override
  public Page<WorkoutResponse> fetch(String status, Pageable pageable) {
    log.info("attempt to fetch Workouts with page: {} and size: {}", pageable.getPageNumber(), pageable.getPageNumber());
    Page<Workout> workoutPage = status == null ? workoutRepository.findAll(pageable) :
        workoutRepository.findByStatus(WorkoutStatus.valueOf(status.toUpperCase()), pageable);

    log.info("workouts fetched successfully");
    return workoutPage.map(workoutMapper::toWorkoutResponse);
  }

  @Override
  public Page<ExerciseDetailsResponse> fetchExercises(Long workoutId, Pageable pageable) {
    log.info("attempt to fetch exercises related to workout with id: {}", workoutId);
    verifyIfExistsWorkout(workoutId);

    return exerciseService.fetchExercises(workoutId, pageable);
  }

  /**
   * Verifica se existe Workout para o dado workoutId.
   * Caso exista Workout, o programa segue o fluxo normal, caso contrário,
   * {@code WorkoutNotFoundException} é lançada.
   *
   * @param workoutId identificador do workout a ser verificado.
   * @throws WorkoutNotFoundException caso não exista Workout para o dado workoutId.
   */
  private void verifyIfExistsWorkout(Long workoutId) {
    if (!workoutRepository.existsById(workoutId))
      throw new WorkoutNotFoundException("workout not found with id: %s".formatted(workoutId));
  }

}
