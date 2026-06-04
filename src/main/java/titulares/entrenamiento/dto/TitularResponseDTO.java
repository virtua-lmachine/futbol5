package titulares.entrenamiento.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de Transferencia de Dato
 * empaquetar y transportar únicamente los datos necesarios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TitularResponseDTO {
    private String nombreJugador;
    private Double puntuacionPromedio;
}
