package titulares.entrenamiento.mapper;

import titulares.entrenamiento.dto.RendimientoRequestDTO;
import titulares.entrenamiento.dto.RendimientoResponseDTO;
import titulares.entrenamiento.model.RendimientoJugador;

public class RendimientoMapper {

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
