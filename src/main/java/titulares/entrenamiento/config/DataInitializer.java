package titulares.entrenamiento.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import titulares.entrenamiento.model.Rol;
import titulares.entrenamiento.model.Usuario;
import titulares.entrenamiento.repository.UsuarioRepository;
import titulares.entrenamiento.security.PasswordHasher;
/**
 *  inicializar datos básicos durante el arranque del sistema.
 * Implementa la interfaz CommandLineRunner para ejecutar lógica automáticamente
 * Su función principal es verificar si existen usuarios registrados en la base de datos
 */
@Component
public class DataInitializer implements CommandLineRunner {
    /**
     * Repositorio utilizado para realizar operaciones de persistencia
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor encargado de inyectar el repositorio de usuarios
     */
    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    /**
     *  ejecutado al iniciar la aplicación, Verifica si la base de datos contiene usuarios registrados.
     * Si no existen registros, crea dos usuarios por defecto:
     * un entrenador y un jugador, asignándoles sus respectivos roles
     * y almacenando sus contraseñas de forma cifrada
     */
    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            // Crear entrenador predeterminado
            Usuario entrenador = Usuario.builder()
                    .username("entrenador")
                    .password(PasswordHasher.hash("entrenador123"))
                    .rol(Rol.ENTRENADOR)
                    .build();
            usuarioRepository.save(entrenador);

            // Crear jugador predeterminado
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
