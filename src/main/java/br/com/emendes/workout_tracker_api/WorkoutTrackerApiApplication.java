package br.com.emendes.workout_tracker_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@SpringBootApplication
public class WorkoutTrackerApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(WorkoutTrackerApiApplication.class, args);
  }

}
