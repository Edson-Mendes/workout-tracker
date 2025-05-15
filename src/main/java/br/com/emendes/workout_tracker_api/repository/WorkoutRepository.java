package br.com.emendes.workout_tracker_api.repository;


import br.com.emendes.workout_tracker_api.model.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface repository com as abstrações para interação com o recurso Workout no banco de dados.
 */
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

}
