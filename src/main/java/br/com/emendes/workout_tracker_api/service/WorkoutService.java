package br.com.emendes.workout_tracker_api.service;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseDetailsResponse;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker_api.validation.annotation.ValidWorkoutStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

/**
 * Interface com as abstrações para manipulação do recurso Workout.
 */
@Validated
public interface WorkoutService {

  /**
   * Cria um Workout.
   *
   * @param workoutCreateRequest objeto contendo os dados para criação de Workout.
   * @return WorkoutResponse contendo informações do Workout criado.
   */
  WorkoutResponse create(@Valid WorkoutCreateRequest workoutCreateRequest);

  /**
   * Adiciona um Exercise a um Workout.
   *
   * @param workoutId             identificador do Workout que terá Exercise adicionado.
   * @param exerciseCreateRequest objeto contendo os dados de criação de Exercise.
   * @return ExerciseResponse contendo as informações do Exercise adicionado.
   */
  ExerciseResponse addExercise(
      @NotNull(message = "workoutId must not be null") Long workoutId,
      @Valid ExerciseCreateRequest exerciseCreateRequest);

  /**
   * Adiciona um Weight ao Exercise que pertence ao Workout com id workoutId.
   *
   * @param workoutId           identificador do Workout.
   * @param exerciseId          identificador do Exercise.
   * @param weightCreateRequest objeto contendo os dados de criação de Weight.
   * @return WeightResponse contento as informações do Weight adicionado.
   */
  WeightResponse addWeight(
      @NotNull(message = "workoutId must not be null") Long workoutId,
      Long exerciseId,
      WeightCreateRequest weightCreateRequest);

  /**
   * Busca paginada de Workouts, opcional busca por status.
   * <br><br>
   * Se o {@code status} for informado, somente os workouts com tal status serão retornados,
   * caso {@code status} for null, todos os workouts (dentro do limite da paginação) serão retornados.
   *
   * @param status   status dos workouts a ser buscado (pode ser null).
   * @param pageable modo como será feita a paginação dos dados.
   * @return {@code Page<WorkoutResponse>} Page com os Workout encontrados.
   */
  Page<WorkoutResponse> fetch(
      @ValidWorkoutStatus(message = "status must be a valid workout status (i.e. ONGOING, FINISHED)") String status,
      @NotNull(message = "pageable must not be null") Pageable pageable);

  /**
   * Busca paginada de Exercises.
   *
   * @param workoutId identificador do Workout relacionado com os exercises.
   * @param pageable  modo como será feita a paginação dos dados.
   * @return {@code Page<ExerciseDetailsResponse>} Page com os exercises encontrados.
   */
  Page<ExerciseDetailsResponse> fetchExercises(Long workoutId, Pageable pageable);

}
