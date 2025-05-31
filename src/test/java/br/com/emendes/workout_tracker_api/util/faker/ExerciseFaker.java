package br.com.emendes.workout_tracker_api.util.faker;

import br.com.emendes.workout_tracker_api.dto.response.ExerciseDetailsResponse;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.model.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.emendes.workout_tracker_api.util.faker.WeightFaker.weight;
import static br.com.emendes.workout_tracker_api.util.faker.WeightFaker.weightResponse;
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
  public static final LocalDateTime EXERCISE_CREATED_AT = LocalDateTime.parse("2025-05-12T10:30:00");
  public static final Pageable EXERCISE_PAGEABLE = PageRequest.of(0, 10);

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
        .createdAt(EXERCISE_CREATED_AT)
        .build();
  }

  /**
   * Cria um objeto Exercise sem id e updatedAt e com os campos name, description,
   * additional, sets, workout e createdAt.
   */
  public static Exercise nonCreatedExercise() {
    return Exercise.builder()
        .name(EXERCISE_NAME)
        .description(EXERCISE_DESCRIPTION)
        .additional(EXERCISE_ADDITIONAL)
        .sets(EXERCISE_SETS)
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
        .createdAt(EXERCISE_CREATED_AT)
        .weight(weight())
        .workout(workout())
        .build();
  }

  /**
   * Cria um objeto {@code Optional<Exercise>} contendo um objeto Exercise.
   */
  public static Optional<Exercise> exerciseOptional() {
    return Optional.of(exercise());
  }

  /**
   * Cria um objeto {@code ExerciseDetailsResponse}.
   */
  public static ExerciseDetailsResponse exerciseDetailsResponse() {
    return ExerciseDetailsResponse.builder()
        .exercise(exerciseResponse())
        .weight(weightResponse())
        .build();
  }

  /**
   * Cria um objeto {@code Page<ExerciseDetailsResponse>}.
   */
  public static Page<ExerciseDetailsResponse> exerciseDetailsResponsePage() {
    return new PageImpl<>(List.of(exerciseDetailsResponse()), EXERCISE_PAGEABLE, 1);
  }

  /**
   * Cria um objeto {@code Page<Exercise>}.
   */
  public static Page<Exercise> exercisePage() {
    return new PageImpl<>(List.of(exercise()), EXERCISE_PAGEABLE, 1);
  }

}
