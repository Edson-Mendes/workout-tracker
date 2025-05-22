package br.com.emendes.workout_tracker_api.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Record DTO com os dados de Exercise.
 *
 * @param id          identificador do Exercise.
 * @param name        nome do Exercise.
 * @param description descrição do Exercise.
 * @param additional  informações adicionais do Exercise.
 * @param sets        número de séries do Exercise.
 * @param createdAt   data de criação do Exercise.
 * @param updatedAt   data de atualização do Exercise.
 */
@Builder
public record ExerciseResponse(
    Long id,
    String name,
    String description,
    String additional,
    int sets,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
