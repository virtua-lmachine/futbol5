package titulares.entrenamiento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rendimientos_jugadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendimientoJugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrenamiento_id", nullable = false)
    @JsonIgnore
    private Entrenamiento entrenamiento;

    @Column(name = "nombre_jugador", nullable = false)
    private String nombreJugador;

    @Column(name = "potencia_tiro", nullable = false)
    private Double potenciaTiro;

    @Column(nullable = false)
    private Double velocidad;

    @Column(name = "pases_efectivos", nullable = false)
    private Integer pasesEfectivos;

    @Column(nullable = false)
    private Double resultado;

    public void calcularResultado() {
        this.resultado = (this.potenciaTiro * 0.20) + (this.velocidad * 0.30) + (this.pasesEfectivos * 0.50);
    }
}
