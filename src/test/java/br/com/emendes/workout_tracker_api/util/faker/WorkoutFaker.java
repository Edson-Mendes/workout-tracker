package br.com.emendes.workout_tracker_api.util.faker;

import br.com.emendes.workout_tracker_api.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker_api.model.WorkoutStatus;
import br.com.emendes.workout_tracker_api.model.entity.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe utilitária que gera objetos relacionados a Workout para uso em testes automatizados.
 */
public class WorkoutFaker {

  public static final long WORKOUT_ID = 1_000L;
  public static final String WORKOUT_NAME = "Leg day";
  public static final String WORKOUT_DESCRIPTION = "Lower body focused workout";
  public static final LocalDateTime WORKOUT_CREATED_AT = LocalDateTime.parse("2025-05-12T10:00:00");
  public static final Pageable WORKOUT_PAGEABLE = PageRequest.of(0, 10);

  private WorkoutFaker() {
  }

  /**
   * Cria um objeto Workout sem id e com os campos name, description, isInUse e createdAt.
   */
  public static Workout nonCreatedWorkout() {
    return Workout.builder()
        .name(WORKOUT_NAME)
        .description(WORKOUT_DESCRIPTION)
        .status(WorkoutStatus.ONGOING)
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
        .status(WorkoutStatus.ONGOING)
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
        .status(WorkoutStatus.ONGOING.toString())
        .createdAt(WORKOUT_CREATED_AT)
        .build();
  }

  /**
   * Cria um objeto {@code Page<Workout>}.
   */
  public static Page<Workout> workoutPage() {
    return new PageImpl<>(List.of(workout()), WORKOUT_PAGEABLE, 1);
  }

}
