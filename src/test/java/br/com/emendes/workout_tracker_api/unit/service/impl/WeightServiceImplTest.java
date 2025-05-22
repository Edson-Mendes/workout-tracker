package br.com.emendes.workout_tracker_api.unit.service.impl;

import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.mapper.WeightMapper;
import br.com.emendes.workout_tracker_api.repository.WeightRepository;
import br.com.emendes.workout_tracker_api.service.impl.WeightServiceImpl;
import br.com.emendes.workout_tracker_api.util.faker.WeightFaker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for WeightServiceImpl")
class WeightServiceImplTest {

  @InjectMocks
  private WeightServiceImpl weightService;
  @Mock
  private WeightMapper weightMapperMock;
  @Mock
  private WeightRepository weightRepositoryMock;

  @Nested
  @DisplayName("Create Method")
  class CreateMethod {

    @Test
    @DisplayName("create must return WeightResponse when create successfully")
    void create_MustReturnWeightResponse_WhenCreateSuccessfully() {
      when(weightMapperMock.toWeight(any(), any())).thenReturn(WeightFaker.nonCreatedWeight());
      when(weightRepositoryMock.save(any())).thenReturn(WeightFaker.weight());
      when(weightMapperMock.toWeightResponse(any())).thenReturn(WeightFaker.weightResponse());

      WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
          .value("60.0")
          .unit("KILOGRAMS")
          .build();

      WeightResponse actualWeightResponse = weightService.create(1_000_000L, weightCreateRequest);

      Assertions.assertThat(actualWeightResponse).isNotNull()
          .hasFieldOrPropertyWithValue("value", "60.0")
          .hasFieldOrPropertyWithValue("unit", "KILOGRAMS")
          .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T11:00:00"));
      assertThat(actualWeightResponse.id()).isNotNull();
    }

  }

}