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

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

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
