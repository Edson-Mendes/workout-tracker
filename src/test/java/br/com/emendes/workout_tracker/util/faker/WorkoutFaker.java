package br.com.emendes.workout_tracker.util.faker;

import br.com.emendes.workout_tracker.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker.model.entity.Workout;

import java.time.LocalDateTime;

/**
 * Classe utilitária que gera objetos relacionados a Workout para uso em testes automatizados.
 */
public class WorkoutFaker {

  public static final long WORKOUT_ID = 1_000L;
  public static final String WORKOUT_NAME = "Leg day";
  public static final String WORKOUT_DESCRIPTION = "Lower body focused workout";
  public static final LocalDateTime WORKOUT_CREATED_AT = LocalDateTime.parse("2025-05-12T10:00:00");

  private WorkoutFaker() {
  }

  /**
   * Cria um objeto Workout sem id e com os campos name, description, isInUse e createdAt.
   */
  public static Workout nonCreatedWorkout() {
    return Workout.builder()
        .name(WORKOUT_NAME)
        .description(WORKOUT_DESCRIPTION)
        .isInUse(true)
        .createdAt(WORKOUT_CREATED_AT)
        .build();
  }

  /**
   * Cria um objeto Workout com todos os campos.
   */
  public static Workout workout() {
    return Workout.builder()
        .id(WORKOUT_ID)
        .name(WORKOUT_NAME)
        .description(WORKOUT_DESCRIPTION)
        .isInUse(true)
        .createdAt(WORKOUT_CREATED_AT)
        .build();
  }

  /**
   * Cria um objeto WorkoutResponse com todos os campos.
   */
  public static WorkoutResponse workoutResponse() {
    return WorkoutResponse.builder()
        .id(WORKOUT_ID)
        .name(WORKOUT_NAME)
        .description(WORKOUT_DESCRIPTION)
        .isInUse(true)
        .createdAt(WORKOUT_CREATED_AT)
        .build();
  }
}
