package br.com.emendes.workout_tracker_api.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Record DTO com os dados de Weight.
 *
 * @param id        identificador do Weight
 * @param value     valor de Weight
 * @param unit      unidade de medida em que está o value
 * @param createdAt data de criação de Weight
 */
@Builder
public record WeightResponse(
    Long id,
    String value,
    String unit,
    LocalDateTime createdAt
) {
}
