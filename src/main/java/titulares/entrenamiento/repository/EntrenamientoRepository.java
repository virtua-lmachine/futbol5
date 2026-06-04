package titulares.entrenamiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import titulares.entrenamiento.model.Entrenamiento;

import java.util.Optional;

/**
 * métodos necesarios para persistir, actualizar, eliminar y buscar registros
 */
@Repository
public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Long> {
    Optional<Entrenamiento> findByNumeroSesion(Integer numeroSesion);
}
