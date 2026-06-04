package titulares.entrenamiento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import titulares.entrenamiento.dto.EntrenamientoRequestDTO;
import titulares.entrenamiento.dto.EntrenamientoResponseDTO;
import titulares.entrenamiento.dto.TitularResponseDTO;
import titulares.entrenamiento.mapper.EntrenamientoMapper;
import titulares.entrenamiento.model.Entrenamiento;
import titulares.entrenamiento.model.RendimientoJugador;
import titulares.entrenamiento.repository.EntrenamientoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * gestionar la lógica de negocio asociada a los entrenamientos, y la evaluación del rendimiento del equipo
 * Proporciona operaciones esenciales para persistir nuevas sesiones de práctica y calcular,
 * datos históricos
 */
@Service
public class EntrenamientoService {
    // acceso a los datos y operaciones de entrenamineto
    private final EntrenamientoRepository entrenamientoRepository;
    // Constructor para la inyección automática de dependencias, Instancia del repositorio de entrenamientos
    @Autowired
    public EntrenamientoService(EntrenamientoRepository entrenamientoRepository) {
        this.entrenamientoRepository = entrenamientoRepository;
    }

    /**
     * Registra una nueva sesión de entrenamiento
     * El proceso verifica previamente que el número de sesión sea único.
     * Convierte el objeto DTO de petición a una entidad, lo almacena y retorna los datos
     * mapeados como respuesta.
     */
    @Transactional
    public EntrenamientoResponseDTO registrarEntrenamiento(EntrenamientoRequestDTO request) {
        if (entrenamientoRepository.findByNumeroSesion(request.getNumeroSesion()).isPresent()) {
            throw new RuntimeException("El entrenamiento con número de sesión " + request.getNumeroSesion() + " ya está registrado");
        }

        Entrenamiento entrenamiento = EntrenamientoMapper.toEntity(request);
        Entrenamiento savedEntrenamiento = entrenamientoRepository.save(entrenamiento);

        return EntrenamientoMapper.toResponseDTO(savedEntrenamiento);
    }

    /**
     * Calcula y obtiene el listado de jugadores recomendados para conformar el equipo titular.
     (al menos 3 sesiones de entrenamiento). Agrupa los rendimientos individuales de cada jugador,
     * calcula su puntuación promedio acumulada y ordena a los atletas
     * en orden descendente para seleccionar a los mejores 5
     */
    @Transactional(readOnly = true)
    public List<TitularResponseDTO> obtenerEquipoTitular() {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findAll();

        if (entrenamientos.size() < 3) {
            throw new RuntimeException("No hay suficiente información. Se requieren al menos 3 entrenamientos.");
        }

        // Mapa que contiene el nombre del jugador -> lista de puntuaciones en todas las sesiones
        Map<String, List<Double>> playerScores = new HashMap<>();

        for (Entrenamiento ent : entrenamientos) {
            for (RendimientoJugador rend : ent.getRendimientos()) {
                playerScores.computeIfAbsent(rend.getNombreJugador(), k -> new ArrayList<>())
                        .add(rend.getResultado());
            }
        }

        List<TitularResponseDTO> candidates = new ArrayList<>();

        for (Map.Entry<String, List<Double>> entry : playerScores.entrySet()) {
            String name = entry.getKey();
            List<Double> scores = entry.getValue();

            double sum = 0.0;
            for (Double s : scores) {
                sum += s;
            }
            double average = scores.isEmpty() ? 0.0 : sum / scores.size();

            // Redondear a dos decimales para una presentación más clara
            double roundedAverage = Math.round(average * 100.0) / 100.0;

            candidates.add(TitularResponseDTO.builder()
                    .nombreJugador(name)
                    .puntuacionPromedio(roundedAverage)
                    .build());
        }

        // Ordenar a los jugadores por puntuación, de mayor a menor
        candidates.sort((c1, c2) -> Double.compare(c2.getPuntuacionPromedio(), c1.getPuntuacionPromedio()));

        // Mostrar los 5 primeros (titulares)
        return candidates.stream()
                .limit(5)
                .collect(Collectors.toList());
    }
}
