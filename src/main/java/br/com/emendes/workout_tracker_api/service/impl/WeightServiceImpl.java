package br.com.emendes.workout_tracker_api.service.impl;

import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.mapper.WeightMapper;
import br.com.emendes.workout_tracker_api.model.entity.Weight;
import br.com.emendes.workout_tracker_api.repository.WeightRepository;
import br.com.emendes.workout_tracker_api.service.WeightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementação de {@link WeightService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WeightServiceImpl implements WeightService {

  private final WeightMapper weightMapper;
  private final WeightRepository weightRepository;

  @Override
  public WeightResponse create(Long exerciseId, WeightCreateRequest weightCreateRequest) {
    log.info("attempt to create weight");
    Weight weight = weightMapper.toWeight(exerciseId, weightCreateRequest);

    weight = weightRepository.save(weight);
    log.info("weight created successfully with id: {}", weight.getId());
    return weightMapper.toWeightResponse(weight);
  }

}
