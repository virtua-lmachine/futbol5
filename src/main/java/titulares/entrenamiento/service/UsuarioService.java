package titulares.entrenamiento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import titulares.entrenamiento.dto.AuthResponse;
import titulares.entrenamiento.dto.LoginRequest;
import titulares.entrenamiento.dto.RegisterRequest;
import titulares.entrenamiento.model.Usuario;
import titulares.entrenamiento.repository.UsuarioRepository;
import titulares.entrenamiento.security.JwtUtil;
import titulares.entrenamiento.security.PasswordHasher;

/**
 * ógica de negocio relacionada con los usuarios, Proporciona las funcionalidades principales de seguridad y acceso,}
 * incluyendo el registro de nuevos usuarios con contraseñas encriptadas y la autenticación
 */
@Service
public class UsuarioService {
    // persistencia en la base de datos
    private final UsuarioRepository usuarioRepository;
    // creación, manipulación y validación de tokens basados en JWT
    private final JwtUtil jwtUtil;

    // Constructor para la inyección de dependencias
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registra un nuevo usuario, verifica que el nombre de usuario no exista previamente, encripta la
     *  contraseña utilizando, almacena el registro en la base de datos y genera automáticamente un token de acceso
     * @param request
     * @return
     */
    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(PasswordHasher.hash(request.getPassword()))
                .rol(request.getRol())
                .build();

        Usuario savedUser = usuarioRepository.save(usuario);
        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRol().name());

        return AuthResponse.builder()
                .token(token)
                .username(savedUser.getUsername())
                .rol(savedUser.getRol().name())
                .build();
    }

    /**
     * Autentica a un usuario (Login). búsqueda optimizada para verificar la existencia
     * del usuario. Posteriormente, compara el hash de la contraseña con la almacenada
     * y genera el token de acceso
     * * @param request Objeto DTO que contiene las credenciales de acceso (username y password).
     * @return Un objeto (AuthResponse) con la sesión activa y el token JWT correspondiente.
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Nombre de usuario o contraseña incorrectos"));

        if (!PasswordHasher.check(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Nombre de usuario o contraseña incorrectos");
        }

        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol().name());

        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .rol(usuario.getRol().name())
                .build();
    }
}
