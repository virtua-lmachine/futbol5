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

@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoController {

    private final EntrenamientoService entrenamientoService;

    @Autowired
    public EntrenamientoController(EntrenamientoService entrenamientoService) {
        this.entrenamientoService = entrenamientoService;
    }

    @PostMapping
    public ResponseEntity<EntrenamientoResponseDTO> registrarEntrenamiento(@Valid @RequestBody EntrenamientoRequestDTO request) {
        EntrenamientoResponseDTO response = entrenamientoService.registrarEntrenamiento(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/titulares")
    public ResponseEntity<List<TitularResponseDTO>> obtenerEquipoTitular() {
        List<TitularResponseDTO> response = entrenamientoService.obtenerEquipoTitular();
        return ResponseEntity.ok(response);
    }
}
