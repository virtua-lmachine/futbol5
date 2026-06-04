package titulares.entrenamiento.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import titulares.entrenamiento.dto.AuthResponse;
import titulares.entrenamiento.dto.LoginRequest;
import titulares.entrenamiento.dto.RegisterRequest;
import titulares.entrenamiento.service.UsuarioService;
/**
 * gestiona el sistema de autenticación y acceso a la aplicación
 * Proporciona los endpoints públicos necesarios para el registro de nuevos usuarios
 * y el inicio de sesión bajo la ruta base /api/auth
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    /**
     * Capa de negocio para la gestión, registro y autenticación de usuarios.
     */
    private final UsuarioService usuarioService;
    /**
     * Constructor para la inyección automática de dependencias de Spring
     */
    @Autowired
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    /**
     * registrar una nueva cuenta de usuario Recibe los datos de registro (username, password, rol)
     * Al finalizar con éxito, genera un token  de acceso inmediato y devuelve la respuesta
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = usuarioService.registrar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    /**
     *autenticación de usuarios existentes (Login)
     * Recibe e inspecciona las credenciales enviadas por el cliente generando un token
     * JWT
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }
}
