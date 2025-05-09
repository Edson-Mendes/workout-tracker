package br.com.emendes.workout_tracker.unit.service.impl;

import br.com.emendes.workout_tracker.dto.request.WorkoutCreateRequest;
import br.com.emendes.workout_tracker.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker.mapper.WorkoutMapper;
import br.com.emendes.workout_tracker.repository.WorkoutRepository;
import br.com.emendes.workout_tracker.service.impl.WorkoutServiceImpl;
import br.com.emendes.workout_tracker.util.faker.WorkoutFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static br.com.emendes.workout_tracker.util.faker.WorkoutFaker.nonCreatedWorkout;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
          .hasFieldOrPropertyWithValue("isInUse", true)
          .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T10:00:00"));
      assertThat(actualWorkoutResponse.id()).isNotNull();
    }

  }

}