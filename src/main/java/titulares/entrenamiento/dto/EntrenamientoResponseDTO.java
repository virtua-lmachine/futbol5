package titulares.entrenamiento.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntrenamientoResponseDTO {
    private Long id;
    private Integer numeroSesion;
    private LocalDate fecha;
    private List<RendimientoResponseDTO> rendimientos;
}
