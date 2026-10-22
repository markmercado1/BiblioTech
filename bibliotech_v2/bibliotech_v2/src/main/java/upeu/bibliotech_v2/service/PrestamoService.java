package upeu.bibliotech_v2.service;

import org.springframework.stereotype.Service;
import upeu.bibliotech_v2.model.Estudiante;
import upeu.bibliotech_v2.model.Libro;
import upeu.bibliotech_v2.model.Prestamo;
import upeu.bibliotech_v2.repository.EstudianteRepository;
import upeu.bibliotech_v2.repository.LibroRepository;
import upeu.bibliotech_v2.repository.PrestamoRepository;

import java.time.LocalDate;

@Service
public class PrestamoService {

    private final EstudianteRepository estudianteRepository;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public PrestamoService(
            EstudianteRepository estudianteRepository,
            LibroRepository libroRepository,
            PrestamoRepository prestamoRepository) {

        this.estudianteRepository = estudianteRepository;
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public boolean puedePrestar(Long estudianteId, Long libroId) {

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() ->
                        new RuntimeException("Estudiante no existe"));

        if (!"ACTIVO".equalsIgnoreCase(estudiante.getEstado())) {
            return false;
        }

        long prestamosActivos =
                prestamoRepository.countByEstudianteIdAndEstado(
                        estudianteId, "ACTIVO");

        if (prestamosActivos >= 3) {
            return false;
        }

        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() ->
                        new RuntimeException("Libro no existe"));

        if (libro.getEjemplaresDisponibles() <= 0) {
            return false;
        }

        return true;
    }

    public Prestamo registrarPrestamo(Long estudianteId, Long libroId) {

        if (!puedePrestar(estudianteId, libroId)) {
            throw new RuntimeException("Préstamo no autorizado");
        }

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow();

        Libro libro = libroRepository.findById(libroId)
                .orElseThrow();

        Prestamo prestamo = new Prestamo(
                estudiante,
                libro,
                LocalDate.now(),
                "ACTIVO"
        );

        libro.setEjemplaresDisponibles(
                libro.getEjemplaresDisponibles() - 1
        );

        libroRepository.save(libro);

        return prestamoRepository.save(prestamo);
    }

}
