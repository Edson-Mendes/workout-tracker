package br.com.emendes.workout_tracker_api.model;

/**
 * Enum para indicar o status dos Workouts.
 */
public enum WorkoutStatus {

  /**
   * Significa que o Workout está em uso atualmente, que faz parte da rotina de treino do usuário.
   */
  ONGOING,
  /**
   * Significa que o Workout NÃO está mais em uso, que já não faz mais parte da rotina de treino do usuário.
   */
  FINISHED

}
