package br.com.emendes.workout_tracker_api.exception;

/**
 * Exception para ser lançada em caso de Workout não encontrado.
 */
public class WorkoutNotFoundException extends RuntimeException {

  public WorkoutNotFoundException(String message) {
    super(message);
  }

}
