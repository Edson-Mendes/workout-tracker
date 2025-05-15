package br.com.emendes.workout_tracker_api.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Record DTO com os dados de Workout.
 *
 * @param id                  identificador do workout.
 * @param name                nome do workout.
 * @param description         descrição do workout.
 * @param isInUse indica se o workout está em uso, true caso esteja, false caso contrário.
 * @param createdAt           data em que foi criado o workout.
 */
@Builder
public record WorkoutResponse(
    Long id,
    String name,
    String description,
    boolean isInUse,
    LocalDateTime createdAt
) {
}
