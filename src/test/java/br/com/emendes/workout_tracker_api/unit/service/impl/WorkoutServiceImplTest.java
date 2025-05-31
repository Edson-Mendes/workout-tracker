package br.com.emendes.workout_tracker_api.unit.service.impl;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseDetailsResponse;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker_api.exception.WorkoutNotFoundException;
import br.com.emendes.workout_tracker_api.mapper.WorkoutMapper;
import br.com.emendes.workout_tracker_api.repository.WorkoutRepository;
import br.com.emendes.workout_tracker_api.service.ExerciseService;
import br.com.emendes.workout_tracker_api.service.impl.WorkoutServiceImpl;
import br.com.emendes.workout_tracker_api.util.faker.WorkoutFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static br.com.emendes.workout_tracker_api.util.ConstantsUtil.DEFAULT_PAGEABLE;
import static br.com.emendes.workout_tracker_api.util.faker.ExerciseFaker.exerciseDetailsResponsePage;
import static br.com.emendes.workout_tracker_api.util.faker.ExerciseFaker.exerciseResponse;
import static br.com.emendes.workout_tracker_api.util.faker.WeightFaker.weightResponse;
import static br.com.emendes.workout_tracker_api.util.faker.WorkoutFaker.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for WorkoutServiceImpl")
class WorkoutServiceImplTest {

  @InjectMocks
  private WorkoutServiceImpl workoutService;
  @Mock
  private WorkoutRepository workoutRepositoryMock;
  @Mock
  private WorkoutMapper workoutMapperMock;
  @Mock
  private ExerciseService exerciseServiceMock;

  @Nested
  @DisplayName("Create Method")
  class CreateMethod {

    @Test
    @DisplayName("create must return WorkoutResponse when create successfully")
    void create_MustReturnWorkoutResponse_WhenCreateSuccessfully() {
      when(workoutMapperMock.toWorkout(any())).thenReturn(nonCreatedWorkout());
      when(workoutRepositoryMock.save(any())).thenReturn(WorkoutFaker.workout());
      when(workoutMapperMock.toWorkoutResponse(any())).thenReturn(WorkoutFaker.workoutResponse());

      WorkoutCreateRequest workoutCreateRequest = WorkoutCreateRequest.builder()
          .name("Leg day")
          .description("Lower body focused workout")
          .build();

      WorkoutResponse actualWorkoutResponse = workoutService.create(workoutCreateRequest);

      assertThat(actualWorkoutResponse).isNotNull()
          .hasFieldOrPropertyWithValue("name", "Leg day")
          .hasFieldOrPropertyWithValue("description", "Lower body focused workout")
          .hasFieldOrPropertyWithValue("status", "ONGOING")
          .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T10:00:00"));
      assertThat(actualWorkoutResponse.id()).isNotNull();
    }

  }

  @Nested
  @DisplayName("AddExercise Method")
  class AddExerciseMethod {

    @Test
    @DisplayName("addExercise must return ExerciseResponse when add successfully")
    void addExercise_MustReturnExerciseResponse_WhenAddSuccessfully() {
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.create(any(), any())).thenReturn(exerciseResponse());

      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .name("Leg press")
          .description("Exercise that focuses on the quadriceps")
          .additional("Apply the Rest in Pause technique in the last set")
          .sets(4)
          .build();

      ExerciseResponse actualExerciseResponse = workoutService.addExercise(1_000L, exerciseCreateRequest);

      assertThat(actualExerciseResponse).isNotNull()
          .hasFieldOrPropertyWithValue("name", "Leg press")
          .hasFieldOrPropertyWithValue("description", "Exercise that focuses on the quadriceps")
          .hasFieldOrPropertyWithValue("additional", "Apply the Rest in Pause technique in the last set")
          .hasFieldOrPropertyWithValue("sets", 4)
          .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T10:30:00"));
      assertThat(actualExerciseResponse.id()).isNotNull();
      assertThat(actualExerciseResponse.updatedAt()).isNull();
    }

    @Test
    @DisplayName("addExercise must throw WorkoutNotFoundException when not exists Workout for given id")
    void addExercise_MusThrowWorkoutNotFoundException_WhenNotExistsWorkoutForGivenID() {
      when(workoutRepositoryMock.existsById(any())).thenReturn(false);

      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .name("Leg press")
          .description("Exercise that focuses on the quadriceps")
          .additional("Apply the Rest in Pause technique in the last set")
          .sets(4)
          .build();

      assertThatExceptionOfType(WorkoutNotFoundException.class)
          .isThrownBy(() -> workoutService.addExercise(9_999L, exerciseCreateRequest))
          .withMessage("workout not found with id: 9999");
    }

  }

  @Nested
  @DisplayName("AddWeight Method")
  class AddWeightMethod {

    @Test
    @DisplayName("addWeight must return WeightResponse when add successfully")
    void addWeight_MustReturnWeightResponse_WhenAddSuccessfully() {
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.addWeight(any(), any())).thenReturn(weightResponse());

      WeightCreateRequest exerciseCreateRequest = WeightCreateRequest.builder()
          .value("60.0")
          .unit("KILOGRAMS")
          .build();

      WeightResponse actualWeightResponse = workoutService
          .addWeight(1_000L, 1_000_000L, exerciseCreateRequest);

      assertThat(actualWeightResponse).isNotNull()
          .hasFieldOrPropertyWithValue("value", "60.0")
          .hasFieldOrPropertyWithValue("unit", "KILOGRAMS")
          .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T11:00:00"));
      assertThat(actualWeightResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("addWeight must throw WorkoutNotFoundException when not exists Workout for given id")
    void addWeight_MusThrowWorkoutNotFoundException_WhenNotExistsWorkoutForGivenID() {
      when(workoutRepositoryMock.existsById(any())).thenReturn(false);

      WeightCreateRequest exerciseCreateRequest = WeightCreateRequest.builder()
          .value("60.0")
          .unit("KILOGRAMS")
          .build();

      assertThatExceptionOfType(WorkoutNotFoundException.class)
          .isThrownBy(() -> workoutService.addWeight(9_999L, 1_000_000L, exerciseCreateRequest))
          .withMessage("workout not found with id: 9999");
    }

  }

  @Nested
  @DisplayName("Fetch Method")
  class FetchMethod {

    @Test
    @DisplayName("fetch must return Page<WorkoutResponse> when status is valid")
    void fetch_MustReturnPageWorkoutResponse_WhenStatusIsValid() {
      when(workoutRepositoryMock.findByStatus(any(), any())).thenReturn(workoutPage());
      when(workoutMapperMock.toWorkoutResponse(any())).thenReturn(workoutResponse());

      Page<WorkoutResponse> actualWorkoutResponsePage = workoutService.fetch("ONGOING", DEFAULT_PAGEABLE);

      verify(workoutRepositoryMock).findByStatus(any(), any());
      verify(workoutMapperMock).toWorkoutResponse(any());

      assertThat(actualWorkoutResponsePage).isNotNull().hasSize(1);
    }

    @Test
    @DisplayName("fetch must return Page<WorkoutResponse> when status is null")
    void fetch_MustReturnPageWorkoutResponse_WhenStatusIsNull() {
      when(workoutRepositoryMock.findAll(any(Pageable.class))).thenReturn(workoutPage());
      when(workoutMapperMock.toWorkoutResponse(any())).thenReturn(workoutResponse());

      Page<WorkoutResponse> actualWorkoutResponsePage = workoutService.fetch(null, DEFAULT_PAGEABLE);

      verify(workoutRepositoryMock).findAll(any(Pageable.class));
      verify(workoutMapperMock).toWorkoutResponse(any());

      assertThat(actualWorkoutResponsePage).isNotNull().hasSize(1);
    }

  }

  @Nested
  @DisplayName("FetchExercises Method")
  class FetchExercisesMethod {

    @Test
    @DisplayName("fetchExercises must return Page<ExerciseDetailsResponse> when fetch successfully")
    void fetchExercises_MustReturnPageExerciseDetailsResponse_WhenFetchSuccessfully() {
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.fetchExercises(any(), any())).thenReturn(exerciseDetailsResponsePage());

      Page<ExerciseDetailsResponse> actualExerciseDetailsResponsePage = workoutService
          .fetchExercises(1_000L, DEFAULT_PAGEABLE);

      verify(workoutRepositoryMock).existsById(any());
      verify(exerciseServiceMock).fetchExercises(any(), any());

      assertThat(actualExerciseDetailsResponsePage).isNotNull().hasSize(1);
    }

    @Test
    @DisplayName("fetchExercises must throw WorkoutNotFoundException when not found Workout for given id")
    void fetchExercises_MustThrowWorkoutNotFoundException_WhenNotFoundWorkoutForGivenId() {
      when(workoutRepositoryMock.existsById(any())).thenReturn(false);

      assertThatExceptionOfType(WorkoutNotFoundException.class)
          .isThrownBy(() -> workoutService.fetchExercises(9_999L, DEFAULT_PAGEABLE))
          .withMessage("workout not found with id: 9999");
    }

  }

}