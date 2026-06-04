package titulares.entrenamiento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * datos proporcionados por el cliente
 */
@Data
public class RendimientoRequestDTO {

    @NotBlank(message = "El nombre del jugador es obligatorio")
    private String nombreJugador;

    @NotNull(message = "La potencia de tiro es obligatoria")
    @Min(value = 0, message = "La potencia de tiro debe ser un valor positivo")
    private Double potenciaTiro;

    @NotNull(message = "La velocidad es obligatoria")
    @Min(value = 0, message = "La velocidad debe ser un valor positivo")
    private Double velocidad;

    @NotNull(message = "Los pases efectivos son obligatorios")
    @Min(value = 0, message = "La cantidad de pases efectivos debe ser mayor o igual a 0")
    private Integer pasesEfectivos;
}
