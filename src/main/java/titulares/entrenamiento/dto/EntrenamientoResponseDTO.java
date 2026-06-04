package titulares.entrenamiento.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * exponer la información, transforma la lista de entidades internas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntrenamientoResponseDTO {
    private Long id;
    private Integer numeroSesion;
    private LocalDate fecha;
    /**
     * Listado de los rendimientos técnicos y físicos de los jugadores que
     * participaron en esta sesión específica, adaptados para la respuesta.
     */
    private List<RendimientoResponseDTO> rendimientos;
}
