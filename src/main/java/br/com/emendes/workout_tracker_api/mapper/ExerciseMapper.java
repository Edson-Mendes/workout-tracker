package br.com.emendes.workout_tracker_api.mapper;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.model.entity.Exercise;

/**
 * Interface com as abstrações para mapeamento de do recurso Exercise.
 */
public interface ExerciseMapper {

  /**
   * Mapeia um objeto {@link ExerciseCreateRequest} para {@link Exercise}.
   * <br><br>
   * O campo {@code Exercise.createdAt} tem o valor padrão o horário de criação de Exercise,
   * e o valor padrão de {@code Exercise.updatedAt} é null.
   *
   * @param exerciseCreateRequest objeto com os dados de criação Exercise.
   * @param workoutId             identificador do Workout relacionado ao Exercise.
   * @return Exercise com os dados de ExerciseCreateRequest.
   */
  Exercise toExercise(Long workoutId, ExerciseCreateRequest exerciseCreateRequest);

  /**
   * Mapeia um objeto {@link Exercise} para {@link ExerciseResponse}.
   *
   * @param exercise objeto que será mapeado.
   * @return ExerciseResponse com as informações de Exercise.
   */
  ExerciseResponse toExerciseResponse(Exercise exercise);

}
