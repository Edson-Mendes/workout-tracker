package br.com.emendes.workout_tracker_api.util.faker;

import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.model.UnitOfMeasurement;
import br.com.emendes.workout_tracker_api.model.entity.Exercise;
import br.com.emendes.workout_tracker_api.model.entity.Weight;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Classe utilitária que gera objetos relacionados a Workout para uso em testes automatizados.
 */
public class WeightFaker {

  private static final Long WEIGHT_ID = 1_000_000_000L;
  private static final BigDecimal WEIGHT_VALUE = new BigDecimal("60.0");
  private static final UnitOfMeasurement WEIGHT_UNIT = UnitOfMeasurement.KILOGRAMS;
  private static final LocalDateTime WEIGHT_CREATED_AT = LocalDateTime.parse("2025-05-12T11:00:00");
  private static final Exercise WEIGHT_EXERCISE = Exercise.builder().id(ExerciseFaker.EXERCISE_ID).build();

  private WeightFaker() {
  }

  /**
   * Cria um objeto Weight sem id e com os campos value, unit, exercise e createdAt.
   */
  public static Weight nonCreatedWeight() {
    return Weight.builder()
        .value(WEIGHT_VALUE)
        .unit(WEIGHT_UNIT)
        .createdAt(WEIGHT_CREATED_AT)
        .exercise(WEIGHT_EXERCISE)
        .build();
  }

  /**
   * Cria um objeto Weight com todos os campos.
   */
  public static Weight weight() {
    return Weight.builder()
        .id(WEIGHT_ID)
        .value(WEIGHT_VALUE)
        .unit(WEIGHT_UNIT)
        .createdAt(WEIGHT_CREATED_AT)
        .exercise(WEIGHT_EXERCISE)
        .build();
  }

  /**
   * Cria um objeto WeightResponse com todos os campos.
   */
  public static WeightResponse weightResponse() {
    return WeightResponse.builder()
        .id(WEIGHT_ID)
        .value(WEIGHT_VALUE.toString())
        .unit(WEIGHT_UNIT.toString())
        .createdAt(WEIGHT_CREATED_AT)
        .build();
  }
}
