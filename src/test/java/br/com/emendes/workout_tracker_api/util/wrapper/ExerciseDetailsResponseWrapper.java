package br.com.emendes.workout_tracker_api.util.wrapper;

import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;

import java.time.LocalDateTime;

/**
 * Record para ser usado em testes automatizados para deserializar JSON
 * que representa {@code ExerciseDetailsResponse}.
 * <br><br>
 * NOTA:<br>
 * Isso é uma gambiarra, pois {@code ExerciseDetailsResponse} está com o campo {@code exercise} anotado com
 * {@code JsonUnwrapped}, assim dá erro ao deserializar de volta.
 */
public record ExerciseDetailsResponseWrapper(
    Long id,
    String name,
    String description,
    String additional,
    int sets,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    WeightResponse weight
) {
}
