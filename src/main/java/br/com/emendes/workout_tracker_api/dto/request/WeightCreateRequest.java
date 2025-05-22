package br.com.emendes.workout_tracker_api.dto.request;

import br.com.emendes.workout_tracker_api.validation.annotation.ValidUnitOfMeasurement;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * Record DTO com os dados para criação de Weight.
 *
 * @param value valor do Weight.
 * @param unit  unidade de medida em que está o value, pode ser KILOGRAMS, POUNDS, HOURS ou MINUTES.
 */
@Builder
public record WeightCreateRequest(
    @Digits(integer = 4, fraction = 1, message = "value is out of bounds (<{integer} digits>.<{fraction} digits>)")
    @NotBlank(message = "value must not be blank")
    String value,
    @NotBlank(message = "unit must not be blank")
    @ValidUnitOfMeasurement(message = "unit must be a valid unit of measurement (i.e. KILOGRAM, POUNDS, HOURS, MINUTES")
    String unit
) {
}
