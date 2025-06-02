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
import br.com.emendes.workout_tracker_api.util.faker.WorkoutFaker;
import br.com.emendes.workout_tracker_api.util.wrapper.ExerciseDetailsResponseWrapper;
import br.com.emendes.workout_tracker_api.util.wrapper.PageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Set;

import static br.com.emendes.workout_tracker_api.util.faker.ExerciseFaker.exerciseDetailsResponsePage;
import static br.com.emendes.workout_tracker_api.util.faker.ExerciseFaker.exerciseResponse;
import static br.com.emendes.workout_tracker_api.util.faker.WeightFaker.weightResponse;
import static br.com.emendes.workout_tracker_api.util.faker.WorkoutFaker.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
          .hasFieldOrPropertyWithValue("status", "ONGOING")
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

  @Nested
  @DisplayName("Fetch Endpoint")
  class FetchEndpoint {

    private static final String FETCH_URI = "/api/v1/workouts";

    @ParameterizedTest
    @ValueSource(strings = {"ONGOING", "ongoing", "OnGoInG"})
    @DisplayName("fetch must return status 200 when fetch by status successfully")
    void fetch_MustReturnStatus200_WhenFetchByStatusSuccessfully(String statusParam) throws Exception {
      when(workoutRepositoryMock.findByStatus(any(), any()))
          .thenReturn(WorkoutFaker.workoutPage());
      when(workoutMapperMock.toWorkoutResponse(any())).thenReturn(WorkoutFaker.workoutResponse());

      mockMvc.perform(get(FETCH_URI).contentType(CONTENT_TYPE).param("status", statusParam))
          .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"ONGOING", "ongoing", "OnGoInG"})
    @DisplayName("fetch must return Page<WorkoutResponse> when fetch by status successfully")
    void fetch_MustReturnPageWorkoutResponse_WhenFetchByStatusSuccessfully(String statusParam) throws Exception {
      when(workoutRepositoryMock.findByStatus(any(), any()))
          .thenReturn(WorkoutFaker.workoutPage());
      when(workoutMapperMock.toWorkoutResponse(any())).thenReturn(WorkoutFaker.workoutResponse());

      String actualResponseBody = mockMvc.perform(get(FETCH_URI).contentType(CONTENT_TYPE).param("status", statusParam))
          .andReturn().getResponse().getContentAsString();

      PageResponse<WorkoutResponse> actualWorkoutResponsePage = mapper
          .readValue(actualResponseBody, new TypeReference<>() {
          });

      assertThat(actualWorkoutResponsePage).isNotNull();
      assertThat(actualWorkoutResponsePage.page()).isNotNull();
      assertThat(actualWorkoutResponsePage.page().size()).isEqualTo(10);
      assertThat(actualWorkoutResponsePage.page().number()).isEqualTo(0);
      assertThat(actualWorkoutResponsePage.page().totalElements()).isEqualTo(1);
      assertThat(actualWorkoutResponsePage.page().totalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("fetch must return status 200 when parameter status is not sent")
    void fetch_MustReturnStatus200_WhenParameterStatusIsNotSent() throws Exception {
      when(workoutRepositoryMock.findAll(any(Pageable.class)))
          .thenReturn(WorkoutFaker.workoutPage());
      when(workoutMapperMock.toWorkoutResponse(any())).thenReturn(WorkoutFaker.workoutResponse());

      mockMvc.perform(get(FETCH_URI).contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("fetch must return Page<WorkoutResponse> when parameter status is not sent")
    void fetch_MustReturnPageWorkoutResponse_WhenParameterStatusIsNotSent() throws Exception {
      when(workoutRepositoryMock.findAll(any(Pageable.class)))
          .thenReturn(WorkoutFaker.workoutPage());
      when(workoutMapperMock.toWorkoutResponse(any())).thenReturn(WorkoutFaker.workoutResponse());

      String actualResponseBody = mockMvc.perform(get(FETCH_URI).contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      PageResponse<WorkoutResponse> actualWorkoutResponsePage = mapper
          .readValue(actualResponseBody, new TypeReference<>() {
          });

      assertThat(actualWorkoutResponsePage).isNotNull();
      assertThat(actualWorkoutResponsePage.page()).isNotNull();
      assertThat(actualWorkoutResponsePage.page().size()).isEqualTo(10);
      assertThat(actualWorkoutResponsePage.page().number()).isEqualTo(0);
      assertThat(actualWorkoutResponsePage.page().totalElements()).isEqualTo(1);
      assertThat(actualWorkoutResponsePage.page().totalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("fetch must return status 400 when parameter status is not a valid WorkoutStatus")
    void fetch_MustReturnStatus400_WhenParameterIsNotAValidWorkoutStatus() throws Exception {
      mockMvc.perform(get(FETCH_URI).contentType(CONTENT_TYPE).param("status", "lorem"))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("fetch must return ProblemDetail when parameter status is not a valid WorkoutStatus")
    void fetch_MustReturnProblemDetail_WhenParameterIsNotAValidWorkoutStatus() throws Exception {
      String actualResponseBody = mockMvc.perform(get(FETCH_URI).contentType(CONTENT_TYPE).param("status", "lorem"))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualResponseBody, ProblemDetail.class);

      assertThat(actualProblemDetail).isNotNull()
          .hasFieldOrPropertyWithValue("title", "Bad request")
          .hasFieldOrPropertyWithValue("detail", "Some fields are invalid")
          .hasFieldOrPropertyWithValue("status", 400);
      assertThat(actualProblemDetail.getProperties()).isNotNull();

      String[] actualFields = ((String) actualProblemDetail.getProperties().get("fields")).split(";");
      String[] actualMessages = ((String) actualProblemDetail.getProperties().get("messages")).split(";");

      assertThat(actualFields).isNotNull().hasSize(1).contains("status");
      assertThat(actualMessages).isNotNull().hasSize(1)
          .contains("status must be a valid workout status (i.e. ONGOING, FINISHED)");
    }

  }

  @Nested
  @DisplayName("Fetch Exercises Endpoint")
  class FetchExercisesEndpoint {

    private static final String FETCH_EXERCISES_URI = "/api/v1/workouts/{workoutId}/exercises";

    @Test
    @DisplayName("fetchExercises must return status 200 when fetch successfully")
    void fetchExercises_MustReturnStatus200_WhenFetchSuccessfully() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.fetchExercises(any(), any())).thenReturn(exerciseDetailsResponsePage());

      mockMvc.perform(get(FETCH_EXERCISES_URI, 1_000L).contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("fetchExercises must return Page<ExerciseDetailsResponse> when fetch successfully")
    void fetchExercises_MustReturnPageExerciseDetailsResponse_WhenFetchSuccessfully() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(true);
      when(exerciseServiceMock.fetchExercises(any(), any())).thenReturn(exerciseDetailsResponsePage());

      String actualResponseBody = mockMvc.perform(get(FETCH_EXERCISES_URI, 1_000L).contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      PageResponse<ExerciseDetailsResponseWrapper> actualExerciseDetailsResponsePage = mapper
          .readValue(actualResponseBody, new TypeReference<>() {
          });

      assertThat(actualExerciseDetailsResponsePage).isNotNull();
      assertThat(actualExerciseDetailsResponsePage.content()).isNotNull().hasSize(1);
      assertThat(actualExerciseDetailsResponsePage.page()).isNotNull();
      assertThat(actualExerciseDetailsResponsePage.page().size()).isEqualTo(10);
      assertThat(actualExerciseDetailsResponsePage.page().number()).isEqualTo(0);
      assertThat(actualExerciseDetailsResponsePage.page().totalElements()).isEqualTo(1);
      assertThat(actualExerciseDetailsResponsePage.page().totalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("fetchExercises must return status 404 when not found Workout for given ID")
    void fetchExercises_MustReturnStatus404_WhenNotFoundWorkoutForGivenId() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(false);

      mockMvc.perform(get(FETCH_EXERCISES_URI, 9_999L).contentType(CONTENT_TYPE))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("fetchExercises must return ProblemDetail when not found Workout for given ID")
    void fetchExercises_MustReturnProblemDetail_WhenNotFoundWorkoutForGivenId() throws Exception {
      when(workoutRepositoryMock.existsById(any())).thenReturn(false);

      String actualResponseBody = mockMvc.perform(get(FETCH_EXERCISES_URI, 9_999L).contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();
      ProblemDetail actualProblemDetail = mapper.readValue(actualResponseBody, ProblemDetail.class);

      assertThat(actualProblemDetail).isNotNull()
          .hasFieldOrPropertyWithValue("title", "Not found")
          .hasFieldOrPropertyWithValue("detail", "workout not found with id: 9999")
          .hasFieldOrPropertyWithValue("status", 404);
    }

  }

}