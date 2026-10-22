package upeu.bibliotech_v2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upeu.bibliotech_v2.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}
