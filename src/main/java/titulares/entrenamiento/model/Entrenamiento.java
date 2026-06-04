package titulares.entrenamiento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una sesión de entrenamiento
 * rendimientos individuales de todos los jugadores
 */
@Entity
@Table(name = "entrenamientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_sesion", nullable = false)
    private Integer numeroSesion;

    @Column(nullable = false)
    private LocalDate fecha;

    // orphanRemoval = true}: Si un rendimiento es removido de esta lista, será eliminado físicamente de la base de datos

    /**
     * Listado con los rendimientos de los jugadores evaluados durante esta sesión de entrenamiento
     * Se inicializa por defecto, la correcta instanciación
     */
    @OneToMany(mappedBy = "entrenamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RendimientoJugador> rendimientos = new ArrayList<>();
}
