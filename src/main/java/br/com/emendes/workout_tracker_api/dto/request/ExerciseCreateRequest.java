package br.com.emendes.workout_tracker_api.dto.request;

import br.com.emendes.workout_tracker_api.validation.annotation.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Record DTO com os dados para criação de Exercise.
 *
 * @param name        nome do Exercise.
 * @param description descrição do Exercise (não obrigatório).
 * @param additional  informação adicional do Exercise (não obrigatório).
 * @param sets        número de sets do Exercise.
 * @param weight      carga usado no Exercise.
 */
@Builder
public record ExerciseCreateRequest(
    @NotBlank(message = "name must not be blank or null")
    @Size(min = 1, max = 100, message = "name must contain between {min} and {max} characters long")
    String name,
    @NullOrNotBlank(message = "description must be null or not be blank")
    @Size(max = 255, message = "description must contain max {max} characters long")
    String description,
    @NullOrNotBlank(message = "additional must be null or not be blank")
    @Size(min = 1, max = 150, message = "additional must contain between {min} and {max} characters long")
    String additional,
    @Positive(message = "sets must be positive")
    int sets,
    @Positive(message = "weight must be positive")
    double weight
) {
}
