package br.com.emendes.workout_tracker_api.unit.service.impl;

import br.com.emendes.workout_tracker_api.dto.request.ExerciseCreateRequest;
import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseDetailsResponse;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.exception.ExerciseNotFoundException;
import br.com.emendes.workout_tracker_api.mapper.ExerciseMapper;
import br.com.emendes.workout_tracker_api.repository.ExerciseRepository;
import br.com.emendes.workout_tracker_api.service.WeightService;
import br.com.emendes.workout_tracker_api.service.impl.ExerciseServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static br.com.emendes.workout_tracker_api.util.ConstantsUtil.DEFAULT_PAGEABLE;
import static br.com.emendes.workout_tracker_api.util.faker.ExerciseFaker.*;
import static br.com.emendes.workout_tracker_api.util.faker.WeightFaker.weightResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for ExerciseServiceImpl")
class ExerciseServiceImplTest {

  @InjectMocks
  private ExerciseServiceImpl exerciseService;
  @Mock
  private ExerciseMapper exerciseMapperMock;
  @Mock
  private ExerciseRepository exerciseRepositoryMock;
  @Mock
  private WeightService weightServiceMock;

  @Nested
  @DisplayName("Create Method")
  class CreateMethod {

    @Test
    @DisplayName("create must return ExerciseResponse when create successfully")
    void create_MustReturnExerciseResponse_WhenCreateSuccessfully() {
      when(exerciseMapperMock.toExercise(any(), any())).thenReturn(nonCreatedExercise());
      when(exerciseRepositoryMock.save(any())).thenReturn(exercise());
      when(exerciseMapperMock.toExerciseResponse(any())).thenReturn(exerciseResponse());

      ExerciseCreateRequest exerciseCreateRequest = ExerciseCreateRequest.builder()
          .name("Leg press")
          .description("Exercise that focuses on the quadriceps")
          .additional("Apply the Rest in Pause technique in the last set")
          .sets(4)
          .build();

      ExerciseResponse actualExerciseResponse = exerciseService.create(1_000L, exerciseCreateRequest);

      Assertions.assertThat(actualExerciseResponse).isNotNull()
          .hasFieldOrPropertyWithValue("name", "Leg press")
          .hasFieldOrPropertyWithValue("description", "Exercise that focuses on the quadriceps")
          .hasFieldOrPropertyWithValue("additional", "Apply the Rest in Pause technique in the last set")
          .hasFieldOrPropertyWithValue("sets", 4)
          .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T10:30:00"));
      assertThat(actualExerciseResponse.id()).isNotNull();
      assertThat(actualExerciseResponse.updatedAt()).isNull();
    }

  }

  @Nested
  @DisplayName("AddWeight Method")
  class AddWeightMethod {

    @Test
    @DisplayName("addWeight must return WeightResponse when add successfully")
    void addWeight_MustReturnWeightResponse_WhenAddSuccessfully() {
      when(exerciseRepositoryMock.findById(any())).thenReturn(exerciseOptional());
      when(weightServiceMock.create(any(), any())).thenReturn(weightResponse());

      WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
          .value("60.0")
          .unit("KILOGRAMS")
          .build();

      WeightResponse actualWeightResponse = exerciseService.addWeight(1_000L, weightCreateRequest);

      verify(exerciseRepositoryMock).findById(any());
      verify(weightServiceMock).create(any(), any());
      verify(exerciseRepositoryMock).save(any());

      assertThat(actualWeightResponse).isNotNull()
          .hasFieldOrPropertyWithValue("value", "60.0")
          .hasFieldOrPropertyWithValue("unit", "KILOGRAMS")
          .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T11:00:00"));
      assertThat(actualWeightResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("addWeight must throw ExerciseNotFoundException when not exists Exercise for given id")
    void addWeight_MusThrowExerciseNotFoundException_WhenNotExistsExerciseForGivenID() {
      when(exerciseRepositoryMock.findById(any())).thenReturn(Optional.empty());

      WeightCreateRequest weightCreateRequest = WeightCreateRequest.builder()
          .value("60.0")
          .unit("KILOGRAMS")
          .build();

      assertThatExceptionOfType(ExerciseNotFoundException.class)
          .isThrownBy(() -> exerciseService.addWeight(9_999_999L, weightCreateRequest))
          .withMessage("exercise not found with id: 9999999");
    }

  }

  @Nested
  @DisplayName("FetchExercises Method")
  class FetchExercises {

    @Test
    @DisplayName("fetchExercises must return Page<ExerciseDetailsResponse> when fetch successfully")
    void fetchExercises_MustReturnPageExerciseDetailsResponse_WhenFetchSuccessfully() {
      when(exerciseRepositoryMock.findByWorkoutId(any(), any())).thenReturn(exercisePage());
      when(exerciseMapperMock.toExerciseResponse(any())).thenReturn(exerciseResponse());

      Page<ExerciseDetailsResponse> actualExerciseDetailsResponsePage = exerciseService
          .fetchExercises(1_000L, DEFAULT_PAGEABLE);

      assertThat(actualExerciseDetailsResponsePage).isNotNull().hasSize(1);
    }

  }

}