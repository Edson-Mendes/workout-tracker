package br.com.emendes.workout_tracker_api.service;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.dto.response.WorkoutResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

}
