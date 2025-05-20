package br.com.emendes.workout_tracker_api.mapper;

import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.model.entity.Weight;

/**
 * Interface com as abstrações para mapeamento de do recurso Weight.
 */
public interface WeightMapper {

  /**
   * Mapeia um objeto {@link WeightCreateRequest} para {@link Weight}.<br>
   * <br>
   * O campo {@code Weight.createdAt} tem o valor padrão o horário de criação de Weight.
   *
   * @param exerciseId          identificador do Exercise relacionado com o Weight.
   * @param weightCreateRequest objeto contendo os dados de criação de Weight
   * @return Weight com os dados de weightCreateRequest.
   */
  Weight toWeight(Long exerciseId, WeightCreateRequest weightCreateRequest);

  /**
   * Mapeia um objeto {@link Weight} para {@link WeightResponse}.
   *
   * @param weight objeto que será mapeado.
   * @return {@code WeightResponse} com as informações de Weight.
   */
  WeightResponse toWeightResponse(Weight weight);

}
