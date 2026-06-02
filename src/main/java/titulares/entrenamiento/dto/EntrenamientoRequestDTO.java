package titulares.entrenamiento.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class EntrenamientoRequestDTO {

    @NotNull(message = "El número de sesión de entrenamiento es obligatorio")
    private Integer numeroSesion;

    @NotEmpty(message = "Debe enviar al menos el rendimiento de un jugador")
    @Valid
    private List<RendimientoRequestDTO> performances;
}
