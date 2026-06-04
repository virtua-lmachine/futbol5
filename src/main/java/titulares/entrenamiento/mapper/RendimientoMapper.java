package titulares.entrenamiento.mapper;

import titulares.entrenamiento.dto.RendimientoRequestDTO;
import titulares.entrenamiento.dto.RendimientoResponseDTO;
import titulares.entrenamiento.model.RendimientoJugador;
/**
 * transformación y mapeo de datos entre los Objetos de Transferencia de DTO y la entidad
 *  para convertir las estructuras de entrada
 * y salida de rendimiento
 */
public class RendimientoMapper {
    /**
     * Además de copiar los atributos del cliente, este
     * método dispara de forma automática la lógica interna de la entidad
     * para establecer el puntaje global antes de su persistencia
     */
    public static RendimientoJugador toEntity(RendimientoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        RendimientoJugador entity = RendimientoJugador.builder()
                .nombreJugador(dto.getNombreJugador())
                .potenciaTiro(dto.getPotenciaTiro())
                .velocidad(dto.getVelocidad())
                .pasesEfectivos(dto.getPasesEfectivos())
                .build();
        entity.calcularResultado();
        return entity;
    }
    /**
     * objeto de respuesta de datos
     * <p>
     * Copia de manera directa el identificador y todas las métricas procesadas del
     * jugador, incluyendo el resultado final calculado
     */
    public static RendimientoResponseDTO toResponseDTO(RendimientoJugador entity) {
        if (entity == null) {
            return null;
        }
        return RendimientoResponseDTO.builder()
                .id(entity.getId())
                .nombreJugador(entity.getNombreJugador())
                .potenciaTiro(entity.getPotenciaTiro())
                .velocidad(entity.getVelocidad())
                .pasesEfectivos(entity.getPasesEfectivos())
                .resultado(entity.getResultado())
                .build();
    }
}
