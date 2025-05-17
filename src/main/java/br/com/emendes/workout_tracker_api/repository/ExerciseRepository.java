package br.com.emendes.workout_tracker_api.repository;

import br.com.emendes.workout_tracker_api.model.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface repository com as abstrações para interação com o recurso Exercise no banco de dados.
 */
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
