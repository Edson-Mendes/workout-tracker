package br.com.emendes.workout_tracker.controller;

import br.com.emendes.workout_tracker.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Classe controller responsável pelo endpoint /api/v1/workout/**.
 */
@RestController
@RequestMapping("/api/v1/workout")
@RequiredArgsConstructor
public class WorkoutController {

  private final WorkoutService workoutService;

  /**
   * Método responsável pelo endpoint POST /api/v1/workout para criação de recurso Workout.
   *
   * @param workoutCreateRequest DTO com os dados de criação de Workout.
   * @param uriBuilder           objeto que mantém o host do endpoint para construção do header location.
   */
  @PostMapping
  ResponseEntity<WorkoutResponse> create(
      @RequestBody WorkoutCreateRequest workoutCreateRequest, UriComponentsBuilder uriBuilder) {
    WorkoutResponse workoutResponse = workoutService.create(workoutCreateRequest);
    URI location = uriBuilder.path("/api/v1/workout/{id}").build(workoutResponse.id());

    return ResponseEntity.created(location).body(workoutResponse);
  }

}
