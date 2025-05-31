package br.com.emendes.workout_tracker_api.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;

/**
 * Record DTO com as informações detalhadas de Exercise.
 *
 * @param exercise contém as informações do Exercise.
 * @param weight   contém as informações da carga do Exercise.
 */
@Builder
public record ExerciseDetailsResponse(
    @JsonUnwrapped
    ExerciseResponse exercise,
    WeightResponse weight
) {
}
