package br.com.emendes.workout_tracker_api.repository;

import br.com.emendes.workout_tracker_api.model.entity.Weight;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface repository com as abstrações para interação com o recurso Weight no banco de dados.
 */
public interface WeightRepository extends JpaRepository<Weight, Long> {
}
