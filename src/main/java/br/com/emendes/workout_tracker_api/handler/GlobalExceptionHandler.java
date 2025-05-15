package br.com.emendes.workout_tracker_api.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Classe responsável por lidar com exceptions ao longo da requisição.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException exception) {
    String fields = exception.getConstraintViolations().stream()
        .map(cv -> {
          String[] pathSplit = cv.getPropertyPath().toString().split("\\.");
          return pathSplit.length > 0 ? pathSplit[pathSplit.length - 1] : "";
        })
        .collect(Collectors.joining(";"));
    String messages = exception.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.joining(";"));

    ProblemDetail problemDetail = ProblemDetail.forStatus(400);
    problemDetail.setTitle("Bad request");
    problemDetail.setDetail("Some fields are invalid");
    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/workout-tracker-api"));
    problemDetail.setProperty("fields", fields);
    problemDetail.setProperty("messages", messages);

    return ResponseEntity.badRequest().body(problemDetail);
  }

}
