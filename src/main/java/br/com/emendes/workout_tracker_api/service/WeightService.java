package br.com.emendes.workout_tracker_api.service;

import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * Interface com as abstrações para manipulação do recurso Weight.
 */
@Validated
public interface WeightService {

  /**
   * Cria um Weight.
   *
   * @param exerciseId          identificador do Exercise relacionado com Weight.
   * @param weightCreateRequest objeto contendo os dados para criação do Weight
   * @return {@code WeightResponse} contendo as informações do Weight criado.
   */
  WeightResponse create(
      @NotNull(message = "exerciseId must not be null") Long exerciseId,
      @Valid WeightCreateRequest weightCreateRequest);

}
