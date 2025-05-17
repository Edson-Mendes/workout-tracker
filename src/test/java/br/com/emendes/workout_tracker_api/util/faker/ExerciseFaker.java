package br.com.emendes.workout_tracker_api.util.faker;

import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.model.entity.Exercise;

import java.time.LocalDateTime;

import static br.com.emendes.workout_tracker_api.util.faker.WorkoutFaker.workout;

/**
 * Classe utilitária que gera objetos relacionados a Workout para uso em testes automatizados.
 */
public class ExerciseFaker {

  public static final long EXERCISE_ID = 1_000_000L;
  public static final String EXERCISE_NAME = "Leg press";
  public static final String EXERCISE_DESCRIPTION = "Exercise that focuses on the quadriceps";
  public static final String EXERCISE_ADDITIONAL = "Apply the Rest in Pause technique in the last set";
  public static final int EXERCISE_SETS = 4;
  public static final double EXERCISE_WEIGHT = 50.0;
  public static final LocalDateTime EXERCISE_CREATED_AT = LocalDateTime.parse("2025-05-12T10:30:00");

  private ExerciseFaker() {
  }

  /**
   * Cria um objeto ExerciseResponse com todos os campos.
   */
  public static ExerciseResponse exerciseResponse() {
    return ExerciseResponse.builder()
        .id(EXERCISE_ID)
        .name(EXERCISE_NAME)
        .description(EXERCISE_DESCRIPTION)
        .additional(EXERCISE_ADDITIONAL)
        .sets(EXERCISE_SETS)
        .weight(EXERCISE_WEIGHT)
        .createdAt(EXERCISE_CREATED_AT)
        .build();
  }

  /**
   * Cria um objeto Exercise sem id e updatedAt e com os campos name, description,
   * additional, sets, weight, workout e createdAt.
   */
  public static Exercise nonCreatedExercise() {
    return Exercise.builder()
        .name(EXERCISE_NAME)
        .description(EXERCISE_DESCRIPTION)
        .additional(EXERCISE_ADDITIONAL)
        .sets(EXERCISE_SETS)
        .weight(EXERCISE_WEIGHT)
        .createdAt(EXERCISE_CREATED_AT)
        .workout(workout())
        .build();
  }

  /**
   * Cria um objeto Exercise com todos os campos (exceto updateAt).
   */
  public static Exercise exercise() {
    return Exercise.builder()
        .id(EXERCISE_ID)
        .name(EXERCISE_NAME)
        .description(EXERCISE_DESCRIPTION)
        .additional(EXERCISE_ADDITIONAL)
        .sets(EXERCISE_SETS)
        .weight(EXERCISE_WEIGHT)
        .createdAt(EXERCISE_CREATED_AT)
        .workout(workout())
        .build();
  }
}
