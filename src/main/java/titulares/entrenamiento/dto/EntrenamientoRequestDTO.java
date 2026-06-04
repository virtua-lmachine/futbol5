package titulares.entrenamiento.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * capturar y validar, agrupa datos generales de la sesión junto con una lista de
 *  los rendimiento
 */
@Data
public class EntrenamientoRequestDTO {
    //identifica de forma única la sesión
    @NotNull(message = "El número de sesión de entrenamiento es obligatorio")
    private Integer numeroSesion;
    // los rendimientos de los jugadores
    @NotEmpty(message = "Debe enviar al menos el rendimiento de un jugador")
    @Valid
    private List<RendimientoRequestDTO> performances;
}
