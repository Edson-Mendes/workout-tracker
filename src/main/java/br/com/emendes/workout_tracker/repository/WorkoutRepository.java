package br.com.emendes.workout_tracker.repository;


import br.com.emendes.workout_tracker.model.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface repository com as abstrações para interação com o recurso Workout no banco de dados.
 */
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

}
