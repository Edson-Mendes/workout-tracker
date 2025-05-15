package br.com.emendes.workout_tracker_api.service;

import br.com.emendes.workout_tracker_api.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.WorkoutResponse;
import jakarta.validation.Valid;
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

}
