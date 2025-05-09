package br.com.emendes.workout_tracker.integration.controller;

import br.com.emendes.workout_tracker.controller.WorkoutController;
import br.com.emendes.workout_tracker.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker.mapper.WorkoutMapper;
import br.com.emendes.workout_tracker.repository.WorkoutRepository;
import br.com.emendes.workout_tracker.service.WorkoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static br.com.emendes.workout_tracker.util.faker.WorkoutFaker.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
            WorkoutController.class, WorkoutService.class
        }),
        @ComponentScan.Filter(classes = {RestControllerAdvice.class})
    }
)
@DisplayName("Integration tests entre WorkoutController and WorkoutService")
class WorkoutControllerIT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockitoBean
  private WorkoutMapper workoutMapperMock;
  @MockitoBean
  private WorkoutRepository workoutRepositoryMock;

  @Nested
  class CreateEndpoint {

    private static final String CREATE_URI = "/api/v1/workout";

    @Test
    @DisplayName("POST /api/v1/workout must return status 201 when create workout successfully")
    void create_MustReturnStatus201_WhenCreateWorkoutSuccessfully() throws Exception {
      when(workoutMapperMock.toWorkout(any())).thenReturn(nonCreatedWorkout());
      when(workoutRepositoryMock.save(any())).thenReturn(workout());
      when(workoutMapperMock.toWorkoutResponse(any())).thenReturn(workoutResponse());

      String requestBody = """
          {
            "name": "Leg day",
            "description": "Lower body focused workout"
          }
          """;

      mockMvc.perform(post(CREATE_URI).contentType("application/json;charset=UTF-8").content(requestBody))
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v1/workout must return WorkoutResponse when create workout successfully")
    void create_MustReturnWorkoutResponse_WhenCreateWorkoutSuccessfully() throws Exception {
      when(workoutMapperMock.toWorkout(any())).thenReturn(nonCreatedWorkout());
      when(workoutRepositoryMock.save(any())).thenReturn(workout());
      when(workoutMapperMock.toWorkoutResponse(any())).thenReturn(workoutResponse());

      String requestBody = """
          {
            "name": "Leg day",
            "description": "Lower body focused workout"
          }
          """;

      String actualResponseBody = mockMvc.perform(post(CREATE_URI).contentType("application/json;charset=UTF-8").content(requestBody))
          .andReturn().getResponse().getContentAsString();
      WorkoutResponse actualWorkoutResponse = mapper.readValue(actualResponseBody, WorkoutResponse.class);

      assertThat(actualWorkoutResponse).isNotNull()
          .hasFieldOrPropertyWithValue("id", 1_000L)
          .hasFieldOrPropertyWithValue("name", "Leg day")
          .hasFieldOrPropertyWithValue("description", "Lower body focused workout")
          .hasFieldOrPropertyWithValue("isInUse", true)
          .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T10:00:00"));
    }

    @Test
    @DisplayName("POST /api/v1/workout must return status 400 when request body has invalid fields")
    void create_MustReturnStatus400_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "name": "Leg day",
            "description": "          "
          }
          """;

      mockMvc.perform(post(CREATE_URI).contentType("application/json;charset=UTF-8").content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/workout must return ProblemDetail when request body has invalid fields")
    void create_MustReturnProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "name": "Leg day",
            "description": "          "
          }
          """;

      String actualResponseBody = mockMvc.perform(post(CREATE_URI).contentType("application/json;charset=UTF-8").content(requestBody))
          .andReturn().getResponse().getContentAsString();
      ProblemDetail actualProblemDetail = mapper.readValue(actualResponseBody, ProblemDetail.class);

      assertThat(actualProblemDetail).isNotNull()
          .hasFieldOrPropertyWithValue("title", "Bad request")
          .hasFieldOrPropertyWithValue("detail", "Some fields are invalid")
          .hasFieldOrPropertyWithValue("status", 400)
          .hasFieldOrProperty("properties");

      String[] actualFields = ((String) actualProblemDetail.getProperties().get("fields")).split(";");
      String[] actualMessages = ((String) actualProblemDetail.getProperties().get("messages")).split(";");

      assertThat(actualFields).isNotNull().hasSize(1).contains("description");
      assertThat(actualMessages).isNotNull().hasSize(1).contains("description must be null or not be blank");
    }

  }

}