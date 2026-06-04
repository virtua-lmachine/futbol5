package titulares.entrenamiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import titulares.entrenamiento.model.RendimientoJugador;

/**
 * la infraestructura de Spring Data JPA
 */
@Repository
public interface RendimientoJugadorRepository extends JpaRepository<RendimientoJugador, Long> {
}
