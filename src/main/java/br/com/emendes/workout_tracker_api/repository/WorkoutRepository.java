package br.com.emendes.workout_tracker_api.repository;

import br.com.emendes.workout_tracker_api.model.WorkoutStatus;
import br.com.emendes.workout_tracker_api.model.entity.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface repository com as abstrações para interação com o recurso Workout no banco de dados.
 */
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

  /**
   * Busca paginada de Workout por status.
   *
   * @param status   WorkoutStatus que o Workout deve ter.
   * @param pageable modo como deve ser feito a paginação.
   * @return {@code Page<Workout>} objetos Workouts encontrados e paginados.
   */
  Page<Workout> findByStatus(WorkoutStatus status, Pageable pageable);

}
