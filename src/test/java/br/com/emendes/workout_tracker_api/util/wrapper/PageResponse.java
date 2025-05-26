package br.com.emendes.workout_tracker_api.util.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Record para mapear response body que são paginados.
 *
 * @param content representa o conjunto dos objetos encontrados.
 * @param page    modo como foi feita a paginação.
 * @param <T>     tipo de objeto do encontrado.
 */
public record PageResponse<T>(List<T> content, PageInfo page) {

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public PageResponse(
      @JsonProperty("content") List<T> content, @JsonProperty("page") PageInfo page) {
    this.content = content;
    this.page = page;
  }

  public record PageInfo(int size, int number, int totalElements, int totalPages) {
  }

}
