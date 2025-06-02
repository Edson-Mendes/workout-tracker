package br.com.emendes.workout_tracker_api.repository;

import br.com.emendes.workout_tracker_api.model.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface repository com as abstrações para interação com o recurso Exercise no banco de dados.
 */
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

  /**
   * Busca paginada de Exercise por workoutId.
   *
   * @param workoutId identificador do Workout relacionado com o exercise.
   * @param pageable  modo como deve ser feita a paginação dos dados.
   * @return {@code Page<Exercise>} Page com os Exercises encontrados.
   */
  Page<Exercise> findByWorkoutId(Long workoutId, Pageable pageable);

}
