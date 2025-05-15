package br.com.emendes.workout_tracker_api.controller;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker_api.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Classe controller responsável pelo endpoint /api/v1/workouts/**.
 */
@RestController
@RequestMapping("/api/v1/workouts")
@RequiredArgsConstructor
public class WorkoutController {

  private final WorkoutService workoutService;

  /**
   * Método responsável pelo endpoint POST /api/v1/workouts para criação de recurso Workout.
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

  /**
   * Método responsável pelo endpoint POST /api/v1/workouts/{workoutId}/exercises
   * para adicionar o recurso Exercise ao Workout.
   *
   * @param workoutId             identificador do Workout que receberá o Exercise.
   * @param exerciseCreateRequest objeto contendo as informações do Exercise que será adicionado.
   * @param uriBuilder            objeto que mantém o host do endpoint para construção do header location.
   */
  @PostMapping("/{workoutId}/exercises")
  ResponseEntity<ExerciseResponse> addExercise(
      @PathVariable("workoutId") Long workoutId,
      @RequestBody ExerciseCreateRequest exerciseCreateRequest,
      UriComponentsBuilder uriBuilder) {
    ExerciseResponse exerciseResponse = workoutService.addExercise(workoutId, exerciseCreateRequest);
    URI location = uriBuilder.path("/api/v1/workouts/{workoutId}/exercises/{exerciseId}")
        .build(workoutId, exerciseResponse.id());

    return ResponseEntity.created(location).body(exerciseResponse);
  }

}
