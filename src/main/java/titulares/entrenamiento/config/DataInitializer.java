package titulares.entrenamiento.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import titulares.entrenamiento.model.Rol;
import titulares.entrenamiento.model.Usuario;
import titulares.entrenamiento.repository.UsuarioRepository;
import titulares.entrenamiento.security.PasswordHasher;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            // Create default coach
            Usuario entrenador = Usuario.builder()
                    .username("entrenador")
                    .password(PasswordHasher.hash("entrenador123"))
                    .rol(Rol.ENTRENADOR)
                    .build();
            usuarioRepository.save(entrenador);

            // Create default player
            Usuario jugador = Usuario.builder()
                    .username("jugador")
                    .password(PasswordHasher.hash("jugador123"))
                    .rol(Rol.JUGADOR)
                    .build();
            usuarioRepository.save(jugador);

            System.out.println("=== [DATA INITIALIZER] Test accounts created ===");
            System.out.println("Coach: username='entrenador', password='entrenador123'");
            System.out.println("Player: username='jugador', password='jugador123'");
        }
    }
}
