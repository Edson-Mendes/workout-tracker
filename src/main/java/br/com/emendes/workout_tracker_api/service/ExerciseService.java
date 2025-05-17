package br.com.emendes.workout_tracker_api.service;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;

/**
 * Interface com as abstrações para manipulação do recurso Exercise.
 */
public interface ExerciseService {

  /**
   * Cria um Exercise.
   *
   * @param exerciseCreateRequest objeto contendo os dados para criação de um Exercise.
   * @param workoutId             identificador do workout relacionado com Exercise.
   * @return ExerciseResponse contendo as informações do Exercise criado.
   */
  ExerciseResponse create(Long workoutId, ExerciseCreateRequest exerciseCreateRequest);

}
