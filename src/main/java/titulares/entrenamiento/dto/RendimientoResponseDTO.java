package titulares.entrenamiento.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendimientoResponseDTO {
    private Long id;
    private String nombreJugador;
    private Double potenciaTiro;
    private Double velocidad;
    private Integer pasesEfectivos;
    private Double resultado;
}
