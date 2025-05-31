package br.com.emendes.workout_tracker_api.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Classe com constantes usados em testes automatizados.
 */
public class ConstantsUtil {

  /**
   * Objeto Pageable padrão com número da página 0, e tamanho da página 10.
   */
  public static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10);

}
