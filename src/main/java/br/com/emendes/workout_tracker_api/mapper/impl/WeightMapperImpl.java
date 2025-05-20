package br.com.emendes.workout_tracker_api.mapper.impl;

import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.mapper.WeightMapper;
import br.com.emendes.workout_tracker_api.model.UnitOfMeasurement;
import br.com.emendes.workout_tracker_api.model.entity.Exercise;
import br.com.emendes.workout_tracker_api.model.entity.Weight;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Implementação de {@link WeightMapper}.
 */
@Component
public class WeightMapperImpl implements WeightMapper {

  @Override
  public Weight toWeight(Long exerciseId, WeightCreateRequest weightCreateRequest) {
    Assert.notNull(exerciseId, "exerciseId must not be null");
    Assert.notNull(weightCreateRequest, "weightCreateRequest must not be null");

    return Weight.builder()
        .value(new BigDecimal(weightCreateRequest.value()))
        .unit(UnitOfMeasurement.valueOf(weightCreateRequest.unit()))
        .createdAt(LocalDateTime.now())
        .exercise(Exercise.builder().id(exerciseId).build())
        .build();
  }

  @Override
  public WeightResponse toWeightResponse(Weight weight) {
    Assert.notNull(weight, "weight must not be null");

    return WeightResponse.builder()
        .id(weight.getId())
        .value(weight.getValue().toString())
        .unit(weight.getUnit().toString())
        .createdAt(weight.getCreatedAt())
        .build();
  }

}
