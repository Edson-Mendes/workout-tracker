package br.com.emendes.workout_tracker_api.exception;

/**
 * Exception para ser lançada em caso de Exercise não encontrado.
 */
public class ExerciseNotFoundException extends RuntimeException {

  public ExerciseNotFoundException(String message) {
    super(message);
  }
}
