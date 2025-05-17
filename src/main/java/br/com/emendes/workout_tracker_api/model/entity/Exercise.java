package br.com.emendes.workout_tracker_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Classe que representa a entidade Exercise do banco de dados.
 */
@Entity
@Table(name = "tb_exercise")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Exercise {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  @EqualsAndHashCode.Include
  private Long id;
  @Column(name = "name", nullable = false, length = 100)
  private String name;
  @Column(name = "description")
  private String description;
  @Column(name = "additional", length = 150)
  private String additional;
  @Column(name = "sets", nullable = false)
  private int sets;
  @Column(name = "weight", nullable = false)
  private double weight;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Workout workout;

}
