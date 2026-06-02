package titulares.entrenamiento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import titulares.entrenamiento.dto.TitularResponseDTO;
import titulares.entrenamiento.model.Entrenamiento;
import titulares.entrenamiento.model.RendimientoJugador;
import titulares.entrenamiento.repository.EntrenamientoRepository;
import titulares.entrenamiento.service.EntrenamientoService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EntrenamientoServiceTest {

    @Mock
    private EntrenamientoRepository entrenamientoRepository;

    @InjectMocks
    private EntrenamientoService entrenamientoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalcularResultadoRendimiento() {
        // Jugador1: Potencia tiro = 10, Velocidad = 5, Pases = 25.
        // Result: 10 * 0.2 + 5 * 0.3 + 25 * 0.5 = 2.0 + 1.5 + 12.5 = 16.0
        RendimientoJugador rend = RendimientoJugador.builder()
                .nombreJugador("Jugador1")
                .potenciaTiro(10.0)
                .velocidad(5.0)
                .pasesEfectivos(25)
                .build();
        rend.calcularResultado();
        assertEquals(16.0, rend.getResultado(), 0.001);
    }

    @Test
    void testObtenerEquipoTitularThrowsExceptionWhenFewerThanThreeTrainings() {
        when(entrenamientoRepository.findAll()).thenReturn(Arrays.asList(
                new Entrenamiento(),
                new Entrenamiento()
        ));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            entrenamientoService.obtenerEquipoTitular();
        });

        assertTrue(exception.getMessage().contains("No hay suficiente información"));
    }

    @Test
    void testObtenerEquipoTitularAlgorithmSuccess() {
        // Expected ranking:
        // Jugador3: 18.9
        // Jugador1: 16.0
        // Jugador2: 14.7
        // Jugador7: 14.6
        // Jugador6: 13.7
        // Top 5: Jugador3, Jugador1, Jugador2, Jugador7, Jugador6.
        // Jugador4 (12.6) and Jugador5 (10.0) are left out.

        List<RendimientoJugador> rendimientos = Arrays.asList(
                createRendimiento("Jugador1", 10.0, 5.0, 25), // 16.0
                createRendimiento("Jugador2", 16.0, 5.0, 20), // 14.7
                createRendimiento("Jugador3", 15.0, 3.0, 30), // 18.9
                createRendimiento("Jugador4", 12.0, 4.0, 18), // 12.6
                createRendimiento("Jugador5", 5.0, 3.0, 14),  // 10.0
                createRendimiento("Jugador6", 9.0, 3.0, 22),  // 13.7
                createRendimiento("Jugador7", 10.0, 2.0, 24)  // 14.6
        );

        Entrenamiento e1 = Entrenamiento.builder().id(1L).numeroSesion(1).fecha(LocalDate.now()).rendimientos(rendimientos).build();
        Entrenamiento e2 = Entrenamiento.builder().id(2L).numeroSesion(2).fecha(LocalDate.now()).rendimientos(rendimientos).build();
        Entrenamiento e3 = Entrenamiento.builder().id(3L).numeroSesion(3).fecha(LocalDate.now()).rendimientos(rendimientos).build();

        when(entrenamientoRepository.findAll()).thenReturn(Arrays.asList(e1, e2, e3));

        List<TitularResponseDTO> titulares = entrenamientoService.obtenerEquipoTitular();

        assertEquals(5, titulares.size());
        
        // Assert sorting order and score
        assertEquals("Jugador3", titulares.get(0).getNombreJugador());
        assertEquals(18.9, titulares.get(0).getPuntuacionPromedio());

        assertEquals("Jugador1", titulares.get(1).getNombreJugador());
        assertEquals(16.0, titulares.get(1).getPuntuacionPromedio());

        assertEquals("Jugador2", titulares.get(2).getNombreJugador());
        assertEquals(14.7, titulares.get(2).getPuntuacionPromedio());

        assertEquals("Jugador7", titulares.get(3).getNombreJugador());
        assertEquals(14.6, titulares.get(3).getPuntuacionPromedio());

        assertEquals("Jugador6", titulares.get(4).getNombreJugador());
        assertEquals(13.7, titulares.get(4).getPuntuacionPromedio());
    }

    private RendimientoJugador createRendimiento(String name, double tiro, double vel, int pases) {
        RendimientoJugador rend = RendimientoJugador.builder()
                .nombreJugador(name)
                .potenciaTiro(tiro)
                .velocidad(vel)
                .pasesEfectivos(pases)
                .build();
        rend.calcularResultado();
        return rend;
    }
}
