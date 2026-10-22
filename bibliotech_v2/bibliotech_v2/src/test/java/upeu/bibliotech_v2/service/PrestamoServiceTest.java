
package upeu.bibliotech_v2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import upeu.bibliotech_v2.model.Estudiante;
import upeu.bibliotech_v2.model.Libro;
import upeu.bibliotech_v2.model.Prestamo;
import upeu.bibliotech_v2.repository.EstudianteRepository;
import upeu.bibliotech_v2.repository.LibroRepository;
import upeu.bibliotech_v2.repository.PrestamoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    private Estudiante estudiante;
    private Libro libro;

    @BeforeEach
    void configurar() {
        estudiante = new Estudiante("Ana", "ACTIVO");
        estudiante.setId(1L);

        libro = new Libro("Clean Code", "Robert C. Martin", 1);
        libro.setId(10L);

        when(estudianteRepository.findById(1L))
                .thenReturn(Optional.of(estudiante));

        when(libroRepository.findById(10L))
                .thenReturn(Optional.of(libro));

        when(prestamoRepository.countByEstudianteIdAndEstado(
                1L, "ACTIVO"))
                .thenReturn(0L);
    }

    @Test
    void debePermitirPrestamoCuandoHayEjemplaresDisponibles() {
        assertFalse(prestamoService.puedePrestar(1L, 10L));
    }

    @Test
    void debeRechazarPrestamoCuandoNoHayEjemplaresDisponibles() {
        libro.setEjemplaresDisponibles(0);

        assertThrows(RuntimeException.class,
                () -> prestamoService.registrarPrestamo(1L, 10L));

        verify(libroRepository, never()).save(any(Libro.class));
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    @Test
    void debeDescontarUnEjemplarAlRegistrarPrestamo() {
        Prestamo prestamo = new Prestamo(
                estudiante, libro, java.time.LocalDate.now(), "ACTIVO");

        when(prestamoRepository.save(any(Prestamo.class)))
                .thenReturn(prestamo);

        Prestamo resultado =
                prestamoService.registrarPrestamo(1L, 10L);

        assertNotNull(resultado);
        assertEquals(0, libro.getEjemplaresDisponibles());

        verify(libroRepository).save(libro);
        verify(prestamoRepository).save(any(Prestamo.class));
    }

    @Test
    void debeRechazarDisponibilidadNegativa() {
        libro.setEjemplaresDisponibles(-1);

        assertFalse(prestamoService.puedePrestar(1L, 10L));
    }
}
