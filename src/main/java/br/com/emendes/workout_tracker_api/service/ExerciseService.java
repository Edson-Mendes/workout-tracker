package br.com.emendes.workout_tracker_api.service;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import jakarta.validation.constraints.NotNull;

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

  /**
   * Adicionar um Weight ao Exercise.
   *
   * @param exerciseId          identificador do Exercise.
   * @param weightCreateRequest objeto contendo os dados para adicionar um Weight.
   * @return WeightResponse objeto com as informações do Weight adicionado.
   */
  WeightResponse addWeight(
      @NotNull(message = "exerciseId must not be null") Long exerciseId,
      WeightCreateRequest weightCreateRequest);

}
