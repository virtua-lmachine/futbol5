package titulares.entrenamiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import titulares.entrenamiento.model.Usuario;

import java.util.Optional;

/**
 * gestionar las operaciones CRUD, para la interacción con la base de datos
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
