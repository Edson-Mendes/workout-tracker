package br.com.emendes.workout_tracker_api.integration.controller;

import br.com.emendes.workout_tracker_api.controller.WorkoutController;
import br.com.emendes.workout_tracker_api.dto.request.WeightCreateRequest;
import br.com.emendes.workout_tracker_api.dto.response.ExerciseResponse;
import br.com.emendes.workout_tracker_api.dto.response.WeightResponse;
import br.com.emendes.workout_tracker_api.dto.response.WorkoutResponse;
import br.com.emendes.workout_tracker_api.exception.ExerciseNotFoundException;
import br.com.emendes.workout_tracker_api.mapper.WorkoutMapper;
import br.com.emendes.workout_tracker_api.repository.WorkoutRepository;
import br.com.emendes.workout_tracker_api.service.ExerciseService;
import br.com.emendes.workout_tracker_api.service.WorkoutService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
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
import java.util.Set;

import static br.com.emendes.workout_tracker_api.util.faker.ExerciseFaker.exerciseResponse;
import static br.com.emendes.workout_tracker_api.util.faker.WeightFaker.weightResponse;
import static br.com.emendes.workout_tracker_api.util.faker.WorkoutFaker.*;
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
  @MockitoBean
  private ExerciseService exerciseServiceMock;

  public static final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Create Endpoint")
  class CreateEndpoint {

    private static final String CREATE_URI = "/api/v1/workouts";

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

      mockMvc.perform(post(CREATE_URI).contentType(CONTENT_TYPE).content(requestBody))
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

      String actualResponseBody = mockMvc.perform(post(CREATE_URI).contentType(CONTENT_TYPE).content(requestBody))
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

      mockMvc.perform(post(CREATE_URI).contentType(CONTENT_TYPE).content(requestBody))
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

      String actualResponseBody = mockMvc.perform(post(CREATE_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();
      ProblemDetail actualProblemDetail = mapper.readValue(actualResponseBody, ProblemDetail.class);

      assertThat(actualProblemDetail).isNotNull()
          .hasFieldOrPropertyWithValue("title", "Bad request")
          .hasFieldOrPropertyWithValue("detail", "Some fields are invalid")
          .hasFieldOrPropertyWithValue("status", 400);
      assertThat(actualProblemDetail.getProperties()).isNotNull();
      String[] actualFields = ((String) actualProblemDetail.getProperties().get("fields")).split(";");
      String[] actualMessages = ((String) actualProblemDetail.getProperties().get("messages")).split(";");

      assertThat(actualFields).isNotNull().hasSize(1).contains("description");
      assertThat(actualMessages).isNotNull().hasSize(1).contains("description must be null or not be blank");
    }

  }

  @Nested
  @DisplayName("Add Exercise Endpoint")
  class AddExerciseEndpoint {

    private static final String ADD_EXERCISE_URI = "/api/v1/workouts/{workoutId}/exercises";

    @Test
    @DisplayName("addExercise must return status 201 when add exercise successfully")
    void addExercise_MustReturnStatus201_WhenAddExerciseSuccessfully() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.create(any(), any())).thenReturn(exerciseResponse());

      String requestBody = """
          {
            "name": "Leg press",
            "description": "Exercise that focuses on the quadriceps",
            "additional": "Apply the Rest in Pause technique in the last set",
            "sets": 4
          }
          """;

      mockMvc.perform(post(ADD_EXERCISE_URI, 1_000L).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("addExercise must return ExerciseResponse when add exercise successfully")
    void addExercise_MustReturnExerciseResponse_WhenAddExerciseSuccessfully() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.create(any(), any())).thenReturn(exerciseResponse());

      String requestBody = """
          {
            "name": "Leg press",
            "description": "Exercise that focuses on the quadriceps",
            "additional": "Apply the Rest in Pause technique in the last set",
            "sets": 4
          }
          """;

      String actualResponseBody = mockMvc.perform(post(ADD_EXERCISE_URI, 1_000L).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();
      ExerciseResponse actualExerciseResponse = mapper.readValue(actualResponseBody, ExerciseResponse.class);

      assertThat(actualExerciseResponse).isNotNull()
          .hasFieldOrPropertyWithValue("id", 1_000_000L)
          .hasFieldOrPropertyWithValue("name", "Leg press")
          .hasFieldOrPropertyWithValue("description", "Exercise that focuses on the quadriceps")
          .hasFieldOrPropertyWithValue("additional", "Apply the Rest in Pause technique in the last set")
          .hasFieldOrPropertyWithValue("sets", 4)
          .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T10:30:00"));
      assertThat(actualExerciseResponse.updatedAt()).isNull();
    }

    @Test
    @DisplayName("addExercise must return status 400 when request body has invalid fields")
    void addExercise_MustReturnStatus400_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "name": "Leg press",
            "description": "   ",
            "additional": "Apply the Rest in Pause technique in the last set",
            "sets": 4
          }
          """;

      mockMvc.perform(post(ADD_EXERCISE_URI, 1_000L).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("addExercise must return ProblemDetail when request body has invalid fields")
    void addExercise_MustReturnProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "name": "Leg press",
            "description": "   ",
            "additional": "Apply the Rest in Pause technique in the last set",
            "sets": 4
          }
          """;

      String actualResponseBody = mockMvc.perform(post(ADD_EXERCISE_URI, 1_000L).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();
      ProblemDetail actualProblemDetail = mapper.readValue(actualResponseBody, ProblemDetail.class);

      assertThat(actualProblemDetail).isNotNull()
          .hasFieldOrPropertyWithValue("title", "Bad request")
          .hasFieldOrPropertyWithValue("detail", "Some fields are invalid")
          .hasFieldOrPropertyWithValue("status", 400);
      assertThat(actualProblemDetail.getProperties()).isNotNull();

      String[] actualFields = ((String) actualProblemDetail.getProperties().get("fields")).split(";");
      String[] actualMessages = ((String) actualProblemDetail.getProperties().get("messages")).split(";");

      assertThat(actualFields).isNotNull().hasSize(1).contains("description");
      assertThat(actualMessages).isNotNull().hasSize(1).contains("description must be null or not be blank");
    }

    @Test
    @DisplayName("addExercise must return status 404 when do not exists Workout with given id")
    void addExercise_MustReturnStatus404_WhenDoNotExistsWorkoutWithGivenId() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(false);
      String requestBody = """
          {
            "name": "Leg press",
            "description": "Exercise that focuses on the quadriceps",
            "additional": "Apply the Rest in Pause technique in the last set",
            "sets": 4
          }
          """;

      mockMvc.perform(post(ADD_EXERCISE_URI, 9_999L).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("addExercise must return ProblemDetail when do not exists Workout with given id")
    void addExercise_MustReturnProblemDetail_WhenDoNotExistsWorkoutWithGivenId() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(false);
      String requestBody = """
          {
            "name": "Leg press",
            "description": "Exercise that focuses on the quadriceps",
            "additional": "Apply the Rest in Pause technique in the last set",
            "sets": 4
          }
          """;

      String actualResponseBody = mockMvc.perform(post(ADD_EXERCISE_URI, 9_999L).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();
      ProblemDetail actualProblemDetail = mapper.readValue(actualResponseBody, ProblemDetail.class);

      assertThat(actualProblemDetail).isNotNull()
          .hasFieldOrPropertyWithValue("title", "Not found")
          .hasFieldOrPropertyWithValue("detail", "workout not found with id: 9999")
          .hasFieldOrPropertyWithValue("status", 404);
    }

  }

  @Nested
  @DisplayName("Add Weight Endpoint")
  class AddWeightEndpoint {

    private static final String ADD_WEIGHT_URI = "/api/v1/workouts/{workoutId}/exercises/{exerciseId}/weights";

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Set<ConstraintViolation<WeightCreateRequest>> getViolations(String jsonObject) throws JsonProcessingException {
      WeightCreateRequest object = mapper.readValue(jsonObject, WeightCreateRequest.class);
      return validator.validate(object);
    }

    @Test
    @DisplayName("addWeight must return status 201 when add weight successfully")
    void addWeight_MustReturnStatus201_WhenAddWeightSuccessfully() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.addWeight(any(), any())).thenReturn(weightResponse());

      String requestBody = """
          {
            "value": "60.0",
            "unit": "KILOGRAMS"
          }
          """;

      mockMvc.perform(post(ADD_WEIGHT_URI, 1_000L, 1_000_000L).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("addWeight must return WeightResponse when add weight successfully")
    void addWeight_MustReturnWeightResponse_WhenAddWeightSuccessfully() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.addWeight(any(), any())).thenReturn(weightResponse());

      String requestBody = """
          {
            "value": "60.0",
            "unit": "KILOGRAMS"
          }
          """;

      String actualResponseBody = mockMvc
          .perform(post(ADD_WEIGHT_URI, 1_000L, 1_000_000L).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();
      WeightResponse actualWeightResponse = mapper.readValue(actualResponseBody, WeightResponse.class);

      assertThat(actualWeightResponse).isNotNull()
          .hasFieldOrPropertyWithValue("id", 1_000_000_000L)
          .hasFieldOrPropertyWithValue("value", "60.0")
          .hasFieldOrPropertyWithValue("unit", "KILOGRAMS")
          .hasFieldOrPropertyWithValue("createdAt", LocalDateTime.parse("2025-05-12T11:00:00"));
    }

    @Test
    @DisplayName("addWeight must return status 400 when request body has invalid fields")
    void addWeight_MustReturnStatus400_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "value": "60.0",
            "unit": "daidjaoidsjoijas"
          }
          """;
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.addWeight(any(), any()))
          .thenThrow(new ConstraintViolationException(getViolations(requestBody)));

      mockMvc.perform(post(ADD_WEIGHT_URI, 1_000L, 1_000_000L).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("addWeight must return ProblemDetail when request body has invalid fields")
    void addWeight_MustReturnProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "value": "60.0",
            "unit": "daidjaoidsjoijas"
          }
          """;
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.addWeight(any(), any()))
          .thenThrow(new ConstraintViolationException(getViolations(requestBody)));

      String actualResponseBody = mockMvc.perform(post(ADD_WEIGHT_URI, 1_000L, 1_000_000L).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();
      ProblemDetail actualProblemDetail = mapper.readValue(actualResponseBody, ProblemDetail.class);

      assertThat(actualProblemDetail).isNotNull()
          .hasFieldOrPropertyWithValue("title", "Bad request")
          .hasFieldOrPropertyWithValue("detail", "Some fields are invalid")
          .hasFieldOrPropertyWithValue("status", 400);
      assertThat(actualProblemDetail.getProperties()).isNotNull();

      String[] actualFields = ((String) actualProblemDetail.getProperties().get("fields")).split(";");
      String[] actualMessages = ((String) actualProblemDetail.getProperties().get("messages")).split(";");

      assertThat(actualFields).isNotNull().hasSize(1).contains("unit");
      assertThat(actualMessages).isNotNull().hasSize(1)
          .contains("unit must be a valid unit of measurement (i.e. KILOGRAM, POUNDS, HOURS, MINUTES");
    }

    @Test
    @DisplayName("addWeight must return status 404 when do not exists Workout with given id")
    void addWeight_MustReturnStatus404_WhenDoNotExistsWorkoutWithGivenId() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(false);
      String requestBody = """
          {
            "value": "60.0",
            "unit": "KILOGRAMS"
          }
          """;

      mockMvc.perform(post(ADD_WEIGHT_URI, 9_999L, 1_000_000L).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("addWeight must return ProblemDetail when do not exists Workout with given id")
    void addWeight_MustReturnProblemDetail_WhenDoNotExistsWorkoutWithGivenId() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(false);
      String requestBody = """
          {
            "value": "60.0",
            "unit": "KILOGRAMS"
          }
          """;

      String actualResponseBody = mockMvc
          .perform(post(ADD_WEIGHT_URI, 9_999L, 1_000_000L).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();
      ProblemDetail actualProblemDetail = mapper.readValue(actualResponseBody, ProblemDetail.class);

      assertThat(actualProblemDetail).isNotNull()
          .hasFieldOrPropertyWithValue("title", "Not found")
          .hasFieldOrPropertyWithValue("detail", "workout not found with id: 9999")
          .hasFieldOrPropertyWithValue("status", 404);
    }

    @Test
    @DisplayName("addWeight must return status 404 when do not exists Exercise with given id")
    void addWeight_MustReturnStatus404_WhenDoNotExistsExerciseWithGivenId() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.addWeight(any(), any()))
          .thenThrow(new ExerciseNotFoundException("exercise not found with id: 9999999"));
      String requestBody = """
          {
            "value": "60.0",
            "unit": "KILOGRAMS"
          }
          """;

      mockMvc.perform(post(ADD_WEIGHT_URI, 1_000L, 9_999_999L).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("addWeight must return ProblemDetail when do not exists Exercise with given id")
    void addWeight_MustReturnProblemDetail_WhenDoNotExistsExerciseWithGivenId() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.addWeight(any(), any()))
          .thenThrow(new ExerciseNotFoundException("exercise not found with id: 9999999"));
      String requestBody = """
          {
            "value": "60.0",
            "unit": "KILOGRAMS"
          }
          """;

      String actualResponseBody = mockMvc
          .perform(post(ADD_WEIGHT_URI, 1_000L, 9_999_999L).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();
      ProblemDetail actualProblemDetail = mapper.readValue(actualResponseBody, ProblemDetail.class);

      assertThat(actualProblemDetail).isNotNull()
          .hasFieldOrPropertyWithValue("title", "Not found")
          .hasFieldOrPropertyWithValue("detail", "exercise not found with id: 9999999")
          .hasFieldOrPropertyWithValue("status", 404);
    }

  }

}