package br.com.emendes.workout_tracker.dto.request;

import br.com.emendes.workout_tracker.validation.annotation.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Record DTO com os dados para criação de Workout.
 *
 * @param name        nome do workout.
 * @param description descrição do workout (não obrigatório).
 */
@Builder
public record WorkoutCreateRequest(
    @NotBlank(message = "name must not be blank or null")
    @Size(min = 1, max = 100, message = "name must contain between {min} and {max} characters long")
    String name,
    @NullOrNotBlank(message = "description must be null or not be blank")
    @Size(max = 255, message = "description must contain max {max} characters long")
    String description
) {
}
