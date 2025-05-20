package br.com.emendes.workout_tracker_api.unit.mapper.impl;

import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.mapper.impl.WeightMapperImpl;
import br.com.emendes.workout_tracker_api.model.UnitOfMeasurement;
import br.com.emendes.workout_tracker_api.model.entity.Exercise;
import br.com.emendes.workout_tracker_api.model.entity.Weight;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit tests for WeightMapperImpl")
class WeightMapperImplTest {

  private final WeightMapperImpl weightMapper = new WeightMapperImpl();

  @Test
  @DisplayName("toWeight must return Weight when map successfully")
  void toWeight_MustReturnWeight_WhenMapSuccessfully() {
    WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
        .value("60.0")
        .unit("KILOGRAMS")
        .build();
    Weight actualWeight = weightMapper.toWeight(1_000_000L, weightCreateRequest);

    assertThat(actualWeight).isNotNull()
        .hasFieldOrPropertyWithValue("value", new BigDecimal("60.0"))
        .hasFieldOrPropertyWithValue("unit", UnitOfMeasurement.KILOGRAMS);
    assertThat(actualWeight.getId()).isNull();
    assertThat(actualWeight.getCreatedAt()).isNotNull();
    assertThat(actualWeight.getExercise()).isNotNull()
        .hasFieldOrPropertyWithValue("id", 1_000_000L);
  }

  @Test
  @DisplayName("toWeight must throw IllegalArgumentException when weightCreateRequest is null")
  void toWeight_MustThrowIllegalArgumentException_WhenWeightCreateRequestIsNull() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> weightMapper.toWeight(1_000_000L, null))
        .withMessage("weightCreateRequest must not be null");
  }

  @Test
  @DisplayName("toWeight must throw IllegalArgumentException when exerciseId is null")
  void toWeight_MustThrowIllegalArgumentException_WhenExerciseIdIsNull() {
    WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
        .value("60.0")
        .unit("KILOGRAM")
        .build();

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> weightMapper.toWeight(null, weightCreateRequest))
        .withMessage("exerciseId must not be null");
  }

  @Test
  @DisplayName("toWeightResponse must return Weight when map successfully")
  void toWeightResponse_MustReturnWeight_WhenMapSuccessfully() {
    Weight weight = Weight.builder()
        .id(1_000_000_000L)
        .value(new BigDecimal("60.0"))
        .unit(UnitOfMeasurement.KILOGRAMS)
        .createdAt(LocalDateTime.parse("2025-05-12T12:00:00"))
        .exercise(Exercise.builder().id(1_000_000L).build())
        .build();
    WeightResponse actualWeightResponse = weightMapper.toWeightResponse(weight);

    assertThat(actualWeightResponse).isNotNull()
        .hasFieldOrPropertyWithValue("id", 1_000_000_000L)
        .hasFieldOrPropertyWithValue("value", "60.0")
        .hasFieldOrPropertyWithValue("unit", "KILOGRAMS")
        .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T12:00:00"));
  }

  @Test
  @DisplayName("toWeightResponse must throw IllegalArgumentException when weight is null")
  void toWeightResponse_MustThrowIllegalArgumentException_WhenWeightIsNull() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> weightMapper.toWeightResponse(null))
        .withMessage("weight must not be null");
  }

}