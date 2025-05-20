package br.com.emendes.workout_tracker_api.model.entity;

import br.com.emendes.workout_tracker_api.model.UnitOfMeasurement;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Classe que representa a entidade Weight do banco de dados.
 */
@Entity
@Table(name = "tb_weight")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Weight {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "value", nullable = false, precision = 5, scale = 1)
  private BigDecimal value;
  @Column(name = "unit", nullable = false)
  @Enumerated(EnumType.STRING)
  private UnitOfMeasurement unit;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Exercise exercise;

}
