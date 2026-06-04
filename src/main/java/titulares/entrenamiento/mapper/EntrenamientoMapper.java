package titulares.entrenamiento.mapper;

import titulares.entrenamiento.dto.EntrenamientoRequestDTO;
import titulares.entrenamiento.dto.EntrenamientoResponseDTO;
import titulares.entrenamiento.dto.RendimientoResponseDTO;
import titulares.entrenamiento.model.Entrenamiento;
import titulares.entrenamiento.model.RendimientoJugador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * transformación y mapeo de datos entre los Objetos
 * Proporciona métodos estáticos que encapsulan la lógica de conversión para desacoplar las capas
 * de Transferencia de Datos (DTO) y la entidad de negocio
 */
public class EntrenamientoMapper {
    /**
     * Transforma un objeto de solicitud en una nueva entidad de negocio
     * se asigna automáticamente la fecha actual y se realiza una conversión en cascada
     * de la lista de rendimientos
     */

    public static Entrenamiento toEntity(EntrenamientoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Entrenamiento entity = Entrenamiento.builder()
                .numeroSesion(dto.getNumeroSesion())
                .fecha(LocalDate.now())
                .build();

        if (dto.getPerformances() != null) {
            List<RendimientoJugador> rendimientos = dto.getPerformances().stream()
                    .map(perfDto -> {
                        RendimientoJugador rend = RendimientoMapper.toEntity(perfDto);
                        if (rend != null) {
                            rend.setEntrenamiento(entity);
                        }
                        return rend;
                    })
                    .collect(Collectors.toList());
            entity.setRendimientos(rendimientos);
        }

        return entity;
    }
    /**
     * objeto de respuesta de datos
     * métricas internas de la sesión y delega de manera iterativa el mapeo de los
     * rendimientos asociados para construir una estructura enviada al cliente.
     */
    public static EntrenamientoResponseDTO toResponseDTO(Entrenamiento entity) {
        if (entity == null) {
            return null;
        }

        List<RendimientoResponseDTO> rendimientosDTO = new ArrayList<>();
        if (entity.getRendimientos() != null) {
            rendimientosDTO = entity.getRendimientos().stream()
                    .map(RendimientoMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }

        return EntrenamientoResponseDTO.builder()
                .id(entity.getId())
                .numeroSesion(entity.getNumeroSesion())
                .fecha(entity.getFecha())
                .rendimientos(rendimientosDTO)
                .build();
    }
}
