package br.com.emendes.workout_tracker.mapper;

import br.com.emendes.workout_tracker.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker.model.entity.Workout;

/**
 * Interface com as abstrações para mapeamento de do recurso Workout.
 */
public interface WorkoutMapper {

  /**
   * Mapeia um objeto {@link WorkoutCreateRequest} para {@link Workout}.
   * <br><br>
   * O campo {@code  Workout.isInUse} tem valor padrão {@code true},
   * e o campo {@code Workout.createdAt} tem o valor padrão o horário da criação de Workout.
   *
   * @param workoutCreateRequest objeto com os dados de criação Workout.
   * @return Workout com os dados de WorkoutCreateRequest.
   */
  Workout toWorkout(WorkoutCreateRequest workoutCreateRequest);

  /**
   * Mapeia um objeto {@link Workout} para {@link WorkoutResponse}.
   *
   * @param workout objeto Workout que será mapeado.
   * @return {@code WorkoutResponse} DTO resposta com os dados de Workout.
   */
  WorkoutResponse toWorkoutResponse(Workout workout);

}
