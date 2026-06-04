package titulares.entrenamiento.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import titulares.entrenamiento.dto.EntrenamientoRequestDTO;
import titulares.entrenamiento.dto.EntrenamientoResponseDTO;
import titulares.entrenamiento.dto.TitularResponseDTO;
import titulares.entrenamiento.service.EntrenamientoService;

import java.util.List;
/**
 * Controlador REST que expone los endpoints de la API para la gestión de entrenamientos
 * y consulta del equipo titular. bajo la ruta base e interactúa con el servicio EntrenamientoService
 */
@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoController {

    private final EntrenamientoService entrenamientoService;
    /**
     * Constructor para la inyección automática de dependencias, contiene la lógica de negocio de los entrenamientos.
     */
    @Autowired
    public EntrenamientoController(EntrenamientoService entrenamientoService) {
        this.entrenamientoService = entrenamientoService;
    }

    /**
     *  registrar una nueva sesión de entrenamiento
     * Recibe los datos de la sesión en el cuerpo de la petición
     */
    @PostMapping
    public ResponseEntity<EntrenamientoResponseDTO> registrarEntrenamiento(@Valid @RequestBody EntrenamientoRequestDTO request) {
        EntrenamientoResponseDTO response = entrenamientoService.registrarEntrenamiento(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * obtener el listado sugerido de los 5 jugadores titulares
     * Recupera el análisis de rendimiento acumulado y procesado por el servicio de negocio,
     * devolviendo el Top 5 de futbolistas ordenados por puntuación promedio
     */
    @GetMapping("/titulares")
    public ResponseEntity<List<TitularResponseDTO>> obtenerEquipoTitular() {
        List<TitularResponseDTO> response = entrenamientoService.obtenerEquipoTitular();
        return ResponseEntity.ok(response);
    }
}
